package jonathanlocke.katalyst.sequencer.serialization.deserializers.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession
import jonathanlocke.katalyst.sequencer.serialization.deserializers.Deserializer
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties

class PropertiesDeserializer<Value : Any>(
    val type: KClass<Value>,
    val conversionRegistry: ConversionRegistry = ConversionRegistry.defaultConversionRegistry,
    val listener: ProblemListener,
    val limiter: SerializationLimiter,
) : Deserializer<Value> {

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(text: String): Value {

        // Create a serialization session,
        val session = SerializationSession()

        // create a new instance of the type,
        val value = type.createInstance()

        // then for each non-blank line,
        text.lineSequence().filterNot { it.isBlank() }.forEach { line ->

            // increase the session line count and bytes read,
            session.nextLine(line)

            // split the line into a path and a value,
            val (propertyPath, valueText) = line.trim().split("=", limit = 2)

            // get the property at the path,
            val property = propertyAtPath(propertyPath)

            // and if there is no property,
            if (property == null) {

                // fail
                listener.fail("Property path '$propertyPath' does not exist in type '${type.simpleName}'")

            } else {

                // otherwise, if there is a conversion for the type,
                val conversions = conversionRegistry[property.returnType.classifier as KClass<*>]
                if (!conversions.isEmpty()) {

                    // get the forward converter (String to Value),
                    val converter = conversions[0].forwardConverter() as Converter<Any, Value>

                    // convert the value text to the type,
                    val converted = converter.convert(valueText, listener)

                    // set the property value
                    property.setter.call(value, converted)

                    // then check if the session limit has reached a limit,
                    if (limiter.isLimitExceeded(session, listener)) {

                        // and report an error if so.
                        listener.fail("Serialization limit exceeded")
                    }
                }
            }
        }

        return value
    }

    /**
     * Gets the mutable property of [type] at the given path.
     */
    private fun propertyAtPath(path: String): KMutableProperty<*>? {

        var at: KClass<*> = type
        var property: KMutableProperty<*>? = null

        // For each element in the path,
        for (element in path.split('.')) {

            // find the property with the element's name,
            property = at
                .memberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .find { it.name == element }

            // and if it is not found,
            if (property == null) {

                // give up,
                return null
            }

            // otherwise advance to the type of the property for the next element.
            at = property.returnType.classifier as KClass<*>
        }

        return property
    }
}
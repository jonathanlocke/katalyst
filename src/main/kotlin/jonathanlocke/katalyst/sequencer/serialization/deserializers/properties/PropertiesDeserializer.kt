package jonathanlocke.katalyst.sequencer.serialization.deserializers.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession
import jonathanlocke.katalyst.sequencer.serialization.deserializers.Deserializer
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
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
        val value = type.java.getDeclaredConstructor().newInstance()

        // then for each non-blank line,
        text.lineSequence().filterNot { it.isBlank() }.forEach { line ->

            // increase the session line count and bytes read,
            session.lines = session.lines + 1
            session.bytes = session.bytes + line.length + 1

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

    private fun propertyAtPath(path: String): KMutableProperty<*>? {
        var at: KClass<*> = type
        var property: KMutableProperty<*>? = null
        for (segment in path.split('.')) {
            property =
                at.memberProperties.filterIsInstance<KMutableProperty<*>>().find { it.name == segment } ?: return null
            at = property.returnType.classifier as? KClass<*> ?: return property
        }
        return property
    }
}
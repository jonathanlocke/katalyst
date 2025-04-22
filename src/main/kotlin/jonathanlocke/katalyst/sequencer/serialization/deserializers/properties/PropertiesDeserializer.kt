package jonathanlocke.katalyst.sequencer.serialization.deserializers.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemException
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession
import jonathanlocke.katalyst.sequencer.serialization.deserializers.Deserializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

class PropertiesDeserializer<Value : Any>(
    val type: KClass<Value>,
    val conversionRegistry: ConversionRegistry = ConversionRegistry.defaultConversionRegistry,
    val listener: ProblemListener<Value>,
    val limiter: SerializationLimiter,
) :
    Deserializer<Value> {

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(text: String): Value {

        // Create a serialization session,
        val session = SerializationSession()

        // create a new instance of the type,
        type.java.getDeclaredConstructor().newInstance()

        // then for each non-blank line,
        text.lineSequence().filterNot { it.isBlank() }.forEach { line ->

            // split the line into a path and a value,
            val (propertyPath, valueText) = line.trim().split("=", limit = 2)

            val property = propertyAtPath(propertyPath)
            if (property == null) {
                listener.error("Property path '$propertyPath' does not exist in type '${type.simpleName}'")
                return@forEach
            } else {

                // and if there is a conversion for the type,
                val conversions = conversionRegistry[property.returnType.classifier as KClass<*>]
                if (!conversions.isEmpty()) {

                    // get the forward converter (String to Value),
                    val converter = conversions[0].forwardConverter() as Converter<Any, Value>

                    // convert the value text to the type,
                    converter.convert(valueText, listener)

                    // then check if the session limit has reached a limit,
                    if (limiter.isLimitExceeded(session, listener)) {

                        // and report an error if so.
                        throw ProblemException("Serialization limit exceeded")
                    }
                }
            }
        }
    }

    private fun propertyAtPath(path: String): KProperty<*>? {
        var at: KClass<*> = type
        var property: KProperty<*>? = null
        for (segment in path.split('.')) {
            property = at.memberProperties.find { it.name == segment } ?: return null
            at = property.returnType.classifier as? KClass<*> ?: return property
        }
        return property
    }
}
package jonathanlocke.katalyst.sequencer.serialization.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.converters.Converter
import jonathanlocke.katalyst.cripsr.reflection.Properties.Companion.kClass
import jonathanlocke.katalyst.cripsr.reflection.PropertyPath
import jonathanlocke.katalyst.cripsr.reflection.PropertyPath.Companion.propertyPath
import jonathanlocke.katalyst.cripsr.reflection.PropertyPath.Companion.rootPropertyPath
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.sequencer.serialization.Deserializer
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Deserializes a properties file to a value.
 *
 * @param type The type to deserialize the properties file to
 * @param conversionRegistry The conversion registry to use for converting properties during deserialization
 * @param limiter The limiter to use to limit the impact of deserialization
 *
 * @see PropertiesSerialization
 */
class PropertiesDeserializer<Value : Any>(
    val type: KClass<Value>,
    val conversionRegistry: ConversionRegistry = defaultConversionRegistry,
    val limiter: SerializationLimiter
) : Deserializer<Value> {

    /**
     * Deserializes a properties file to a value of the given [type] following these steps:
     *
     * 1. Create an instance of [type]
     * 2. For each non-blank line in the properties file, split the line into a path and a value
     * 3. Get the property at the path
     * 4. Convert the value to the property's type
     * 5. Set the property's value to the converted value
     * 6. Tell the serialization session that we processed a property
     * 7. If the serialization session limit has reached any limit, report an error and fail the deserialization
     *
     * @param listener A problem listener to report problems to
     * @param text The properties file text to deserialize
     * @return The deserialized value
     */
    @Suppress("UNCHECKED_CAST")
    override fun deserialize(listener: ProblemListener, text: String): Value {

        // Create a serialization session,
        val session = PropertiesSerializationSession()

        // create a new instance of the type,
        val value = type.createInstance()
        val pathToValue = mutableMapOf<PropertyPath, Any>()
        pathToValue[rootPropertyPath] = value

        // then for each non-blank line,
        text.lineSequence().filterNot { it.isBlank() }.forEach { propertyText ->

            // split the line into a path and a value,
            val (propertyPathText, valueText) = propertyText.trim().split("=", limit = 2)

            // get the property at the path,
            val propertyPath = propertyPath(propertyPathText)
            val property = propertyPath.property(type)

            // and if there is no property,
            if (property == null) {

                // fail
                listener.fail("Property path '$propertyPathText' does not exist in type '${type.simpleName}'")

            } else {

                // otherwise, if there is a conversion for the type,
                val conversions = conversionRegistry.to(property.kClass())
                if (!conversions.isEmpty()) {

                    // get the forward converter (String to Value),
                    val converter = conversions.first().forwardConverter() as Converter<Any, Value>

                    // convert the value text to the type,
                    val converted = converter.convert(valueText, listener)

                    // set the property value
                    property.setter.call(value, converted)

                    // tell the session that we processed a property,
                    session.processedProperty(propertyText)

                    // then ensure that the session limit has not been reached
                    limiter.ensureLimitNotReached(session, listener)

                } else {

                    // since there is no conversion, create an instance of the property's type,
                    val child = property.kClass().createInstance()

                    // and keep it in the child paths map for later,
                    pathToValue[propertyPath] = child

                    // and store the child
                    property.setter.call(pathToValue[propertyPath.parent()], child)
                }
            }
        }

        return value
    }
}
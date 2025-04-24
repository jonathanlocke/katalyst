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
    val limiter: PropertiesSerializationLimiter
) : Deserializer<Value> {

    /**
     * Deserializes individual properties
     */
    internal inner class PropertyDeserializer(val listener: ProblemListener) {

        /**
         * The root value we're deserializing
         */
        val value = type.createInstance()

        /**
         * The last property path we processed.
         */
        var lastPath: PropertyPath = rootPropertyPath(Any::class)

        val pathToValue = mutableMapOf<PropertyPath, Any>()

        init {
            pathToValue[lastPath] = value
        }

        /**
         * A property is deserialized by the following steps:
         *
         * 1.
         * 2. For each non-blank line in the properties file, split the line into a path and a value
         * 3. Get the property at the path
         * 4. Convert the value to the property's type
         * 5. Set the property's value to the converted value
         */
        fun deserialize(path: PropertyPath, valueText: String) {

            // Get the property at the given path in the value's type,
            val property = path.property()

            // and if there is no property,
            if (property == null) {

                // fail
                listener.fail("Property path does not exist: $path")

            } else {

                // otherwise, get the path of the parent of the property we're deserializing,'
                val parentPath = path.parent()

                // and if the path is deeper than the last path we processed,
                if (path.size > lastPath.size && lastPath.isNotEmpty()) {

                    // first ensure the path only increases by one level at a time,
                    if (path.size > lastPath.size + 1) {
                        listener.fail("Property path '$path' skips levels - can only increase depth by one level at a time")
                        return
                    }

                    // then get the parent property,
                    val parentProperty = parentPath.property()
                    if (parentProperty == null) {
                        listener.fail("Parent property path '$parentPath' does not exist in type '${type.simpleName}'")
                        return
                    }

                    // and initialize it to a new value,
                    val parentValue = parentProperty.kClass().createInstance()
                    val grandparentValue = pathToValue[parentPath.parent()]
                    parentProperty.setter.call(grandparentValue, parentValue)
                    pathToValue[parentPath] = parentValue
                }

                // then if there is a conversion to the property type,
                val conversions = conversionRegistry.to(property.kClass())
                if (!conversions.isEmpty()) {

                    // get the forward converter (String to Value),
                    val converter = conversions.first().forwardConverter() as Converter<Any, *>

                    // convert the value text to the type,
                    val converted = converter.convert(valueText, listener)

                    // set the property value
                    val instance = pathToValue[path.parent()]
                    if (instance != null) {
                        path.value(instance, converted)
                    }
                }

                lastPath = path
            }
        }
    }

    /**
     * Deserializes a properties file to a value of the given [type] following these steps:
     *
     * 1. Create a serialization session and a property deserializer
     * 2. For each non-blank line in the properties file, split the line into a path and a value
     * 3. Deserialize the property
     * 4. Tell the serialization session that we processed a property
     * 5. If the serialization session limit has reached any limit, report an error and fail the deserialization
     *
     * @param listener A problem listener to report problems to
     * @param text The properties file text to deserialize
     * @return The deserialized value
     */
    override fun deserialize(text: String, listener: ProblemListener): Value {

        // Create a serialization session and a property deserializer,
        val session = PropertiesSerializationSession()
        val propertyDeserializer = PropertyDeserializer(listener)

        // then for each non-blank line,
        text.lineSequence().filterNot { it.isBlank() }.forEach { propertyText ->

            // split the line into a property path and a value,
            val (propertyPathText, valueText) = propertyText.trim().split("=", limit = 2)

            // deserialize the property,
            if (limiter.canDeserialize(valueText)) {

                // todo: add ValueClass.property(PropertyPath)
                // todo: add Property.annotations support
                
                propertyDeserializer.deserialize(propertyPath(type, propertyPathText), valueText)
            }

            // tell the session that we processed the property,
            session.processedProperty(propertyText)

            // then ensure that the session limit has not been reached
            limiter.ensureLimitNotReached(session, listener)
        }

        return propertyDeserializer.value
    }
}
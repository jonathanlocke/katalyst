package jonathanlocke.katalyst.serialization.formats.properties

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.PropertyPath
import jonathanlocke.katalyst.reflection.properties.PropertyPath.Companion.propertyPath
import jonathanlocke.katalyst.reflection.properties.PropertyPath.Companion.rootPropertyPath
import jonathanlocke.katalyst.serialization.Deserializer
import jonathanlocke.katalyst.serialization.SerializationLimiter

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
    val type: ValueType<Value>,
    val conversionRegistry: ConversionRegistry = defaultConversionRegistry,
    val limiter: SerializationLimiter
) : Deserializer<Value> {

    /**
     * Deserializes individual properties
     */
    internal inner class PropertyDeserializer(val problemHandler: ProblemHandler) {

        /**
         * The root value we're deserializing
         */
        val value = type.createInstance()

        /**
         * The last property path we processed.
         */
        var lastPath: PropertyPath = rootPropertyPath(valueType(Any::class))

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
                problemHandler.fail("Property path does not exist: $path")

            } else {

                // otherwise, get the path of the parent of the property we're deserializing,'
                val parentPath = path.parent()

                // and if the path is deeper than the last path we processed,
                if (path.size > lastPath.size && lastPath.isNotEmpty()) {

                    // first ensure the path only increases by one level at a time,
                    if (path.size > lastPath.size + 1) {
                        problemHandler.fail("Property path '$path' skips levels - can only increase depth by one level at a time")
                        return
                    }

                    // then get the parent property,
                    val parentProperty = parentPath.property()
                    if (parentProperty == null) {
                        problemHandler.fail("Parent property path '$parentPath' does not exist in type '${type.simpleName}'")
                    } else {
                        // and initialize it to a new value,
                        val parentValue = parentProperty.type().createInstance()
                        val grandparentValue =
                            pathToValue[parentPath.parent()] ?: run {
                                problemHandler.fail("Internal error: no path to grandparent")
                                return
                            }
                        @Suppress("UNCHECKED_CAST")
                        (parentProperty as Property<Any?>).set(grandparentValue, parentValue)
                        pathToValue[parentPath] = parentValue
                    }
                }

                // then if there is a conversion to the property type,
                val conversions = conversionRegistry.to(property.type())
                if (!conversions.isEmpty()) {

                    // get the forward converter (String to Value),
                    val converter = conversions.first().forwardConverter() as Converter<Any, *>

                    // convert the value text to the type,
                    val converted = converter.convert(valueText, problemHandler)

                    // set the property value
                    val instance = pathToValue[path.parent()]
                    if (instance != null) {
                        val property = path.property()
                        if (property != null) {
                            @Suppress("UNCHECKED_CAST")
                            (property as Property<Any?>).set(instance, converted)
                        } else {
                            problemHandler.fail("Internal error: no property at path")
                        }
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
     * @param problemHandler A problem handler to report problems to
     * @param text The properties file text to deserialize
     * @return The deserialized value
     */
    override fun deserialize(text: String, problemHandler: ProblemHandler): Value {

        // Create a serialization session and a property deserializer,
        val session = PropertiesSerializationSession()
        val propertyDeserializer = PropertyDeserializer(problemHandler)

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
            limiter.ensureLimitNotReached(session, problemHandler)
        }

        return propertyDeserializer.value
    }
}
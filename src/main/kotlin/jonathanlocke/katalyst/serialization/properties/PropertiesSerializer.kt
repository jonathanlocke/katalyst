package jonathanlocke.katalyst.serialization.properties

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.reflection.properties.PropertyWalker
import jonathanlocke.katalyst.serialization.SerializationLimiter
import jonathanlocke.katalyst.serialization.Serializer

/**
 * Serializes a value to a properties file.
 *
 * @param conversionRegistry The conversion registry to use for converting properties during serialization
 * @param limiter The limiter to use to limit the impact of serialization
 */
@Suppress("UNCHECKED_CAST")
class PropertiesSerializer<Value : Any>(
    val conversionRegistry: ConversionRegistry = defaultConversionRegistry,
    val limiter: SerializationLimiter
) : Serializer<Value> {

    /**
     * Serializes a value to text in properties file format by following these steps:
     *
     * 1. Walk the properties of the value's type recursively
     * 2. Convert each property to text with the given [conversionRegistry]
     * 3. Add the property to a list of lines in properties file format
     * 4. Tell the serialization session that we processed a property
     * 5. If the serialization session limit has reached any limit, report an error and fail the serialization
     *
     * Serializes a value to a properties file by reflecting on the value's properties and converting them to text
     * with the given [conversionRegistry].
     * @param listener A problem listener to report problems to
     * @param value The value to serialize
     * @return The serialized properties file as a string, with each property on a new line
     */
    @Suppress("UNCHECKED_CAST")
    override fun serialize(value: Value, listener: ProblemListener): String {

        val lines = mutableListOf<String>()

        // Create a serialization session,
        val session = PropertiesSerializationSession()

        // then walk the properties of the value recursively,
        PropertyWalker(value).walk { path, property, value ->

            // convert the value to text,
            val text = toText(listener, value)

            // and if conversion succeeded,
            if (text != null) {

                // create a properties file line in key/value format,
                val line = "${path.pathString()}=$text"

                // add it to the list of lines,
                lines.add(line)

                // tell the session that we added the line,
                session.processedProperty(line)

                // then ensure that the session limit has not been reached
                limiter.ensureLimitNotReached(session, listener)
            }
        }

        return lines.joinToString("\n")
    }

    /**
     * Converts a value to a properties file string with the given [conversionRegistry].
     * @param listener A problem listener to report problems to
     * @param value The value to convert
     * @return The converted value as a string
     */
    @Suppress("UNCHECKED_CAST")
    private fun toText(listener: ProblemListener, value: Any?): String? {

        // If the value is null,
        if (value == null) {

            // then return "null"
            return "null"

        } else {

            // otherwise, get the list of conversions for the value,
            val conversions = conversionRegistry.to(valueType(value::class))

            // and if there is a conversion,
            return if (!conversions.isEmpty()) {

                // get the reverse converter (Value to String),
                val converter = conversions.first().reverseConverter() as Converter<Any, String?>

                // and convert the value to text,
                converter.convert(value, listener) ?: "null"

            } else {

                // otherwise there is no conversion for this property.
                null
            }
        }
    }
}
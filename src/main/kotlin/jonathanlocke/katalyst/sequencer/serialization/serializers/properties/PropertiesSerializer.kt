package jonathanlocke.katalyst.sequencer.serialization.serializers.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.cripsr.reflection.PropertyWalker
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession
import jonathanlocke.katalyst.sequencer.serialization.serializers.Serializer
import kotlin.reflect.KClass

class PropertiesSerializer<Value : Any>(
    val conversionRegistry: ConversionRegistry = defaultConversionRegistry,
    val listener: ProblemListener,
    val limiter: SerializationLimiter,
) : Serializer<Value> {

    @Suppress("UNCHECKED_CAST")
    override fun serialize(value: Value): String {

        val lines = mutableListOf<String>()

        // Create a serialization session,
        val session = SerializationSession()

        // then walk the properties of the value recursively,
        PropertyWalker(value).walk { property, type, path, value ->

            // convert the value to a string,
            val text = convertToString(type, value)

            // and add a key-value pair to list of lines,
            val line = "$path=$text"
            lines.add(line)

            // increase the session line count and bytes read,
            session.nextLine(line)

            // and check if the session limit has reached a limit,
            if (limiter.isLimitExceeded(session, listener)) {

                // and report an error if so.
                listener.error("Serialization limit exceeded")
                return@walk
            }
        }

        return lines.joinToString("\n")
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertToString(type: KClass<*>, value: Any?): String {

        // Get the list of conversions for the type,
        val conversions = conversionRegistry[type]

        // and if there is no conversion,
        var text: String
        if (conversions.isEmpty()) {

            // just do a toString,
            text = value.toString()

        } else {

            // otherwise, get the reverse converter (Value to String),
            val converter = conversions[0].reverseConverter() as Converter<Any, String?>

            // and convert the value to text,
            text = converter.convert(value, listener) ?: "null"
        }

        return text
    }
}
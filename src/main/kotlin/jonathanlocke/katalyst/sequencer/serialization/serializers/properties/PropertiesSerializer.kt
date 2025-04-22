package jonathanlocke.katalyst.sequencer.serialization.serializers.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.cripsr.reflection.PropertyWalker
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession
import jonathanlocke.katalyst.sequencer.serialization.serializers.Serializer

class PropertiesSerializer<Value : Any>(
    val conversionRegistry: ConversionRegistry = ConversionRegistry.defaultConversionRegistry,
    val reporter: ProblemReporter<Value>,
    val limiter: SerializationLimiter,
) :
    Serializer<Value> {

    @Suppress("UNCHECKED_CAST")
    override fun serialize(value: Value): String {

        val lines = mutableListOf<String>()

        // Create a serialization session,
        val session = SerializationSession()

        // then walk the properties of the value recursively,
        PropertyWalker(value).walk { property, type, path, value ->

            // and if there is a conversion for the type,
            val conversions = conversionRegistry[type]
            if (!conversions.isEmpty()) {

                // get the reverse converter (Value to String),
                val converter = conversions[0].reverseConverter() as Converter<Any, Value>

                // convert the value to text,
                val text = converter.convert(value, reporter)

                // and add a line to the properties lines,
                lines.add("$path=$text")

                // then check if the session limit has reached a limit,
                if (limiter.isLimitExceeded(session, reporter)) {

                    // and report an error if so.
                    reporter.error("Serialization limit exceeded")
                    return@walk
                }
            }
        }

        return lines.joinToString("\n")
    }
}
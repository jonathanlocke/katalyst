package jonathanlocke.katalyst.sequencer.serialization.deserializers.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemException
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession
import jonathanlocke.katalyst.sequencer.serialization.deserializers.Deserializer
import kotlin.reflect.KClass

class PropertiesDeserializer<Value : Any>(
    val type: KClass<Value>,
    val conversionRegistry: ConversionRegistry = ConversionRegistry.defaultConversionRegistry,
    val reporter: ProblemReporter<Value>,
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

            val (_, valueText) = line.trim().split("=", limit = 2)

            // and if there is a conversion for the type,
            val conversions = conversionRegistry[type]
            if (!conversions.isEmpty()) {

                // get the forward converter (String to Value),
                val converter = conversions[0].forwardConverter() as Converter<Any, Value>

                // convert the value text to the type,
                converter.convert(valueText, reporter)

                // then check if the session limit has reached a limit,
                if (limiter.isLimitExceeded(session, reporter)) {

                    // and report an error if so.
                    throw ProblemException("Serialization limit exceeded")
                }
            }
        }
    }
}
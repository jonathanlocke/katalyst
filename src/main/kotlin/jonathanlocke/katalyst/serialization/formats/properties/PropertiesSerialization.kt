package jonathanlocke.katalyst.serialization.formats.properties

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.megabytes
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.throwOnError
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.serialization.*
import jonathanlocke.katalyst.serialization.limiters.SizeSerializationLimiter

/**
 * Serialization of a value to a properties file with the format:
 *
 * ```
 * [property-path]=[value]
 * ```
 *
 * For example:
 *
 * ```
 * user.username=Jonathan
 * user.password=password
 * ```
 *
 * @param conversionRegistry The conversion registry to use for converting properties during serialization
 * @param limiter The limiter to use to limit the impact of serialization
 *
 * @see PropertiesSerializer
 * @see PropertiesDeserializer
 * @see ConversionRegistry
 * @see SerializationLimiter
 * @see SerializationLimits
 */
class PropertiesSerialization<Value : Any>(
    val conversionRegistry: ConversionRegistry = defaultConversionRegistry,
    val limiter: SerializationLimiter = defaultPropertiesSerializationLimits
) : Serialization<Value> {

    companion object {

        /**
         * Default serialization limits for [PropertiesSerialization]
         *
         * - Maximum serialization size of 100 MB
         * - Maximum number of properties of 10,000
         *
         * @see SizeSerializationLimiter
         * @see PropertiesSerializationLimiter
         * @see SerializationLimits
         * @see PropertiesSerializer
         * @see PropertiesDeserializer
         */
        val defaultPropertiesSerializationLimits = SerializationLimits(
            SizeSerializationLimiter(megabytes(100)),
            PropertiesSerializationLimiter(count(10_000))
        )
    }

    /**
     * Serializes a value to a properties file
     * @param problemHandler A problem handler to report problems to
     * @param value The value to serialize
     * @return The serialized properties file as a string, with each property on a new line
     *
     * @see PropertiesSerializer
     */
    fun serialize(value: Value, problemHandler: ProblemHandler = throwOnError): String {
        return serializer().serialize(problemHandler, value)
    }

    /**
     * Deserializes a properties file to a value
     * @param problemHandler A problem handler to report problems to
     * @param type The type to deserialize the properties file to
     * @param text The properties file to deserialize
     * @return The deserialized value
     *
     * @see PropertiesDeserializer
     */
    fun deserialize(type: ValueType<Value>, text: String, problemHandler: ProblemHandler = throwOnError): Value {
        return deserializer(type).deserialize(text, problemHandler)
    }

    /**
     * Serializer that serializes a value to a properties file
     */
    override fun serializer(): Serializer<Value> = PropertiesSerializer(conversionRegistry, limiter)

    /**
     * Deserializer that deserializes a properties file to a value
     */
    override fun deserializer(type: ValueType<Value>): Deserializer<Value> = PropertiesDeserializer(
        type, conversionRegistry, limiter
    )
}


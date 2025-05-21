package jonathanlocke.katalyst.serialization.formats.properties

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.megabytes
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.serialization.*
import jonathanlocke.katalyst.serialization.limiters.SizeSerializationLimiter
import jonathanlocke.katalyst.status.StatusHandlerMixin

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
    val limiter: SerializationLimiter = defaultPropertiesSerializationLimits,
) : Serialization<Value>, StatusHandlerMixin {

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
            SizeSerializationLimiter(megabytes(100)), PropertiesSerializationLimiter(count(10_000))
        )
    }

    /**
     * Serializes a value to a properties file
     * @param value The value to serialize
     * @return The serialized properties file as a string, with each property on a new line
     *
     * @see PropertiesSerializer
     */
    fun serialize(value: Value): String {
        return serializer().serialize(value)
    }

    /**
     * Deserializes a properties file to a value
     * @param type The type to deserialize the properties file to
     * @param text The properties file to deserialize
     * @return The deserialized value
     *
     * @see PropertiesDeserializer
     */
    fun deserialize(type: ValueType<Value>, text: String): Value {
        return deserializer(type).deserialize(text)
    }

    /**
     * Serializer that serializes a value to a properties file
     */
    override fun serializer(): Serializer<Value> = handleStatusOf(PropertiesSerializer(conversionRegistry, limiter))

    /**
     * Deserializer that deserializes a properties file to a value
     */
    override fun deserializer(type: ValueType<Value>): Deserializer<Value> =
        handleStatusOf(PropertiesDeserializer(type, conversionRegistry, limiter))
}


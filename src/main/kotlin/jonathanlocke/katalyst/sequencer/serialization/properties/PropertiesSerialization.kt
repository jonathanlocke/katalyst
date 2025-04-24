package jonathanlocke.katalyst.sequencer.serialization.properties

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.defaultConversionRegistry
import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.values.bytes.Bytes
import jonathanlocke.katalyst.nucleus.values.count.Count
import jonathanlocke.katalyst.sequencer.serialization.*
import jonathanlocke.katalyst.sequencer.serialization.limiters.SizeSerializationLimiter

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
            SizeSerializationLimiter(Bytes.Companion.megabytes(100)),
            PropertiesSerializationLimiter(Count.Companion.count(10_000))
        )
    }

    /**
     * Serializes a value to a properties file
     * @param listener A problem listener to report problems to
     * @param value The value to serialize
     * @return The serialized properties file as a string, with each property on a new line
     *
     * @see PropertiesSerializer
     */
    fun serialize(value: Value, listener: ProblemListener = Throw()): String {
        return serializer().serialize(value, listener)
    }

    /**
     * Deserializes a properties file to a value
     * @param listener A problem listener to report problems to
     * @param type The type to deserialize the properties file to
     * @param text The properties file to deserialize
     * @return The deserialized value
     *
     * @see PropertiesDeserializer
     */
    fun deserialize(type: ValueClass<Value>, text: String, listener: ProblemListener = Throw()): Value {
        return deserializer(type).deserialize(text, listener)
    }

    /**
     * Serializer that serializes a value to a properties file
     */
    override fun serializer(): Serializer<Value> = PropertiesSerializer(conversionRegistry, limiter)

    /**
     * Deserializer that deserializes a properties file to a value
     */
    override fun deserializer(type: ValueClass<Value>): Deserializer<Value> = PropertiesDeserializer(
        type, conversionRegistry, limiter
    )
}


package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.reflection.ValueType

/**
 * A serialization implements both a [Serializer] and a [Deserializer]
 */
interface Serialization<Value : Any> {

    /**
     * The [Value] serializer
     */
    fun serializer(): Serializer<Value>

    /**
     * The [Value] deserializer
     */
    fun deserializer(type: ValueType<Value>): Deserializer<Value>
}
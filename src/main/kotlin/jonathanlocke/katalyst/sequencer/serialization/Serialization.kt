package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.cripsr.reflection.PropertyClass

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
    fun deserializer(type: PropertyClass<Value>): Deserializer<Value>
}
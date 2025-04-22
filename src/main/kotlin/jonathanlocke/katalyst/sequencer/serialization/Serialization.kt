package jonathanlocke.katalyst.sequencer.serialization

import kotlin.reflect.KClass

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
    fun deserializer(type: KClass<Value>): Deserializer<Value>
}
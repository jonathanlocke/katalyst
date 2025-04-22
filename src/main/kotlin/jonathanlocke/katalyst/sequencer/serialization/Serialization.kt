package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.sequencer.serialization.deserializers.Deserializer
import jonathanlocke.katalyst.sequencer.serialization.serializers.Serializer

interface Serialization<Value> {

    fun serializer(): Serializer<Value>
    fun deserializer(): Deserializer<Value>
}
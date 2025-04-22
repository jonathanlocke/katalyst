package jonathanlocke.katalyst.sequencer.serialization.deserializers

interface Deserializer<Value> {
    fun deserialize(text: String): Value
}
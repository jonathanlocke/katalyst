package jonathanlocke.katalyst.sequencer.serialization.serializers

interface Serializer<Value> {

    fun serialize(value: Value): String
}
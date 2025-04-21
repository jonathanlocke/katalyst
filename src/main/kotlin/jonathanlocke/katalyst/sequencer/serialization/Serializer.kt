package jonathanlocke.katalyst.sequencer.serialization

interface Serializer {

    fun serialize(obj: Any): String

    fun deserialize(str: String): Any
}
package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandlerMixin

/**
 * Serializes a value to a string
 */
interface Serializer<Value> : StatusHandlerMixin {

    /**
     * Serializes a value to a string
     * @param value The value to serialize
     * @return The serialized string
     */
    fun serialize(value: Value): String
}

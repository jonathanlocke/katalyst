package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerMixin

/**
 * Deserializes a string to a value
 *
 * @see Serialization
 * @see StatusHandler
 */
interface Deserializer<Value> : StatusHandlerMixin {

    /**
     * Deserializes a string to a value
     * @param text The string to deserialize
     * @return The deserialized value
     */
    fun deserialize(text: String): Value
}

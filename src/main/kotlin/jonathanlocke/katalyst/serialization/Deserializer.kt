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
     * @param statusHandler A status handler to report problems to
     * @param text The string to deserialize
     * @return The deserialized value
     */
    fun deserialize(text: String, statusHandler: StatusHandler = this): Value
}
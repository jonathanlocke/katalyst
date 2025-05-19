package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers.Companion.throwOnError

/**
 * Deserializes a string to a value
 *
 * @see Serialization
 * @see StatusHandler
 */
interface Deserializer<Value> {

    /**
     * Deserializes a string to a value
     * @param statusHandler A status handler to report problems to
     * @param text The string to deserialize
     * @return The deserialized value
     */
    fun deserialize(text: String, statusHandler: StatusHandler = throwOnError): Value
}
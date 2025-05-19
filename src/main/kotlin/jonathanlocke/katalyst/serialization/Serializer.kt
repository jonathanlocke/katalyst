package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers.Companion.throwOnError

/**
 * Serializes a value to a string
 */
interface Serializer<Value> {

    /**
     * Serializes a value to a string
     * @param statusHandler A status handler to report problems to
     * @param value The value to serialize
     * @return The serialized string
     */
    fun serialize(value: Value, statusHandler: StatusHandler = throwOnError): String
}
package jonathanlocke.katalyst.serialization.limiters

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.serialization.SerializationLimiter
import jonathanlocke.katalyst.serialization.SerializationSession
import jonathanlocke.katalyst.status.StatusHandlerMixin

/**
 * Limits the maximum size of a serialization
 *
 * @property maximumSize The maximum size
 */
class SizeSerializationLimiter(val maximumSize: Bytes) : SerializationLimiter, StatusHandlerMixin {

    /**
     * Determines whether or not a serialization process should continue
     *
     * @param session The serialization session to inspect
     * @param statusHandler A status handler to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    override fun limitReached(
        session: SerializationSession,
    ): Boolean {
        if (session.processed > maximumSize) {
            error("Serialization exceeded the maximum size of $maximumSize")
            return true
        }
        return false
    }
}
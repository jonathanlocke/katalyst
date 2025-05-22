package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerMixin

/**
 * Provides the ability to limit the size of a serialization.
 *
 * @see SerializationSession
 * @see StatusHandler
 */
interface SerializationLimiter : StatusHandlerMixin {

    /**
     * Override this method if you want to limit the deserialization of individual properties from text
     */
    fun canDeserialize(text: String) = true

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @return True if the serialization should continue, false otherwise
     */
    fun limitReached(session: SerializationSession): Boolean

    /**
     * Ensures that the serialization limit has not been reached.
     * @param session The serialization session to inspect
     */
    fun ensureLimitNotReached(session: SerializationSession) {

        // If the session limit has reached its limit,
        if (limitReached(session)) {

            // fail the serialization process entirely.
            fail("Serialization limit exceeded")
        }
    }
}

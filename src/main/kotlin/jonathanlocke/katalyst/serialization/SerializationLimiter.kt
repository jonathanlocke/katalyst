package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandler

/**
 * Provides the ability to limit the size of a serialization.
 *
 * @see SerializationSession
 * @see StatusHandler
 */
interface SerializationLimiter {

    /**
     * Override this method if you want to limit the deserialization of individual properties from text
     */
    fun canDeserialize(text: String) = true

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param statusHandler A status handler to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    fun limitReached(session: SerializationSession, statusHandler: StatusHandler): Boolean

    /**
     * Ensures that the serialization limit has not been reached.
     * @param session The serialization session to inspect
     * @param statusHandler A status handler to report problems to
     */
    fun ensureLimitNotReached(session: SerializationSession, statusHandler: StatusHandler) {

        // If the session limit has reached its limit,
        if (limitReached(session, statusHandler)) {

            // fail the serialization process entirely.
            statusHandler.fail("Serialization limit exceeded")
        }
    }
}
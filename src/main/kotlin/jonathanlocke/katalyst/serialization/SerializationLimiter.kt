package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemHandler

/**
 * Provides the ability to limit the size of a serialization.
 *
 * @see SerializationSession
 * @see ProblemHandler
 */
interface SerializationLimiter {

    /**
     * Override this method if you want to limit the deserialization of individual properties from text
     */
    fun canDeserialize(text: String) = true

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param problemHandler A problem handler to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    fun limitReached(session: SerializationSession, problemHandler: ProblemHandler): Boolean

    /**
     * Ensures that the serialization limit has not been reached.
     * @param session The serialization session to inspect
     * @param problemHandler A problem handler to report problems to
     */
    fun ensureLimitNotReached(session: SerializationSession, problemHandler: ProblemHandler) {

        // If the session limit has reached its limit,
        if (limitReached(session, problemHandler)) {

            // fail the serialization process entirely.
            problemHandler.fail("Serialization limit exceeded")
        }
    }
}
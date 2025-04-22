package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.problems.ProblemListener

/**
 * Provides the ability to limit the size of a serialization.
 *
 * @see SerializationSession
 * @see ProblemListener
 */
interface SerializationLimiter {

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param listener A problem listener to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    fun limitReached(session: SerializationSession, listener: ProblemListener): Boolean

    /**
     * Ensures that the serialization limit has not been reached.
     * @param session The serialization session to inspect
     * @param listener A problem listener to report problems to
     */
    fun ensureLimitNotReached(session: SerializationSession, listener: ProblemListener) {

        // If the session limit has reached its limit,
        if (limitReached(session, listener)) {

            // fail the serialization process entirely.
            listener.fail("Serialization limit exceeded")
        }
    }
}
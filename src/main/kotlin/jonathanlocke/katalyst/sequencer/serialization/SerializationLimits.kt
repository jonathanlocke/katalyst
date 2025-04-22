package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.problems.ProblemListener

/**
 * A [SerializationLimiter] that combines multiple other [SerializationLimiter]s.
 */
open class SerializationLimits(vararg val limiters: SerializationLimiter) : SerializationLimiter {

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param listener A problem listener to report problems to
     */
    override fun limitReached(
        session: SerializationSession,
        listener: ProblemListener
    ): Boolean {

        // If not all limiters allow the serialization to continue,
        if (limiters.any { it.limitReached(session, listener) }) {

            // fail the serialization.
            listener.fail("Serialization limit exceeded")
            return true
        }
        return false
    }
}
package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemHandler

/**
 * A [SerializationLimiter] that combines multiple other [SerializationLimiter]s.
 */
open class SerializationLimits(open vararg val limiters: SerializationLimiter) : SerializationLimiter {

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param problemHandler A problem handler to report problems to
     */
    override fun limitReached(
        session: SerializationSession,
        problemHandler: ProblemHandler
    ): Boolean {

        // If not all limiters allow the serialization to continue,
        if (limiters.any { it.limitReached(session, problemHandler) }) {

            // fail the serialization.
            problemHandler.fail("Serialization limit exceeded")
            return true
        }
        return false
    }
}
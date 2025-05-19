package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandler

/**
 * A [SerializationLimiter] that combines multiple other [SerializationLimiter]s.
 */
open class SerializationLimits(open vararg val limiters: SerializationLimiter) : SerializationLimiter {

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param statusHandler A status handler to report problems to
     */
    override fun limitReached(
        session: SerializationSession,
        statusHandler: StatusHandler,
    ): Boolean {

        // If not all limiters allow the serialization to continue,
        if (limiters.any { it.limitReached(session, statusHandler) }) {

            // fail the serialization.
            statusHandler.fail("Serialization limit exceeded")
            return true
        }
        return false
    }
}
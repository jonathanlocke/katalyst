package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.status.StatusHandlerMixin

/**
 * A [SerializationLimiter] that combines multiple other [SerializationLimiter]s.
 */
open class SerializationLimits(open vararg val limiters: SerializationLimiter) : SerializationLimiter,
    StatusHandlerMixin {

    init {
        for (limiter in limiters) {
            handleStatusOf(limiter)
        }
    }

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param statusHandler A status handler to report problems to
     */
    override fun limitReached(
        session: SerializationSession,
    ): Boolean {

        // If not all limiters allow the serialization to continue,
        if (limiters.any { it.limitReached(session) }) {

            // fail the serialization.
            fail("Serialization limit exceeded")
            return true
        }
        return false
    }
}
package jonathanlocke.katalyst.sequencer.serialization.limiters

import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession

/**
 * Limits the maximum size of a serialization
 *
 * @property maximumSize The maximum size
 */
class SizeSerializationLimiter(val maximumSize: Bytes) : SerializationLimiter {

    /**
     * Determines whether or not a serialization process should continue
     *
     * @param session The serialization session to inspect
     * @param listener A problem listener to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    override fun limitReached(
        session: SerializationSession,
        listener: ProblemListener
    ): Boolean {
        if (session.processed > maximumSize) {
            listener.error("Serialization exceeded the maximum size of $maximumSize")
            return true
        }
        return false
    }
}
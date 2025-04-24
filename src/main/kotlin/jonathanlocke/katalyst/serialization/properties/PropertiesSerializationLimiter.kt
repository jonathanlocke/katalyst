package jonathanlocke.katalyst.serialization.properties

import jonathanlocke.katalyst.data.values.count.Count
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.serialization.SerializationLimiter
import jonathanlocke.katalyst.serialization.SerializationSession

/**
 * Limits the maximum number of properties in a serialization
 *
 * @property maximumProperties The maximum number of properties
 */
class PropertiesSerializationLimiter(val maximumProperties: Count) : SerializationLimiter {

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param listener A problem listener to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    override fun limitReached(
        session: SerializationSession,
        listener: ProblemListener
    ): Boolean {
        if ((session as PropertiesSerializationSession).properties > maximumProperties) {
            listener.error("Serialization exceeded the maximum of $maximumProperties properties")
            return true
        }
        return false
    }
}
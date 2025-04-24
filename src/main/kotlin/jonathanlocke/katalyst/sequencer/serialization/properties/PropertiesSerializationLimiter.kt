package jonathanlocke.katalyst.sequencer.serialization.properties

import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.values.count.Count
import jonathanlocke.katalyst.sequencer.serialization.SerializationLimiter
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession

/**
 * Limits the maximum number of properties in a serialization
 *
 * @property maximumProperties The maximum number of properties
 */
class PropertiesSerializationLimiter(val maximumProperties: Count) : SerializationLimiter {

    /**
     * Override this method if you want to limit the deserialization of a properties file.
     */
    fun canDeserialize(text: String) = true

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
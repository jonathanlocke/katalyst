package jonathanlocke.katalyst.serialization.formats.properties

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.serialization.SerializationLimiter
import jonathanlocke.katalyst.serialization.SerializationSession
import jonathanlocke.katalyst.status.StatusHandlerMixin

/**
 * Limits the maximum number of properties in a serialization
 *
 * @property maximumProperties The maximum number of properties
 */
class PropertiesSerializationLimiter(val maximumProperties: Count) : SerializationLimiter, StatusHandlerMixin {

    /**
     * Determines whether or not a serialization process should continue
     * @param session The serialization session to inspect
     * @param statusHandler A status handler to report problems to
     * @return True if the serialization should continue, false otherwise
     */
    override fun limitReached(
        session: SerializationSession,
    ): Boolean {
        if ((session as PropertiesSerializationSession).processedProperties > maximumProperties) {
            error("Serialization exceeded the maximum of $maximumProperties properties")
            return true
        }
        return false
    }
}
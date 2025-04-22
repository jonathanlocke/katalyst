package jonathanlocke.katalyst.sequencer.serialization.properties

import jonathanlocke.katalyst.nucleus.values.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.count
import jonathanlocke.katalyst.sequencer.serialization.SerializationSession

/**
 * A serialization session for [PropertiesSerialization]. Tracks the number of properties serialized/deserialized.
 */
class PropertiesSerializationSession : SerializationSession() {

    /**
     * The number of properties serialized/deserialized so far
     */
    var properties = count(0)

    /**
     * Increments the number of properties serialized/deserialized
     * @param propertyText The text of the property
     */
    fun processedProperty(propertyText: String) {

        // Increase the number of properties processed,
        properties++

        // and the number of bytes processed.
        processed(bytes(propertyText.length + 1))
    }
}
package jonathanlocke.katalyst.serialization.formats.properties

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.serialization.SerializationSession

/**
 * A serialization session for [PropertiesSerialization]. Tracks the number of properties serialized/deserialized.
 */
class PropertiesSerializationSession : SerializationSession() {

    /**
     * The number of properties serialized/deserialized so far
     */
    var processedProperties = count(0)

    /**
     * Increments the number of properties serialized/deserialized
     * @param propertyText The text of the property
     */
    fun processedProperty(propertyText: String) {

        // Increase the number of properties processed,
        processedProperties++

        // and the number of bytes processed.
        processed(bytes(propertyText.length + 1))
    }
}

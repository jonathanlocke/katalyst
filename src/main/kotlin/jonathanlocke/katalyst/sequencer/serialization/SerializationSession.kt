package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.values.bytes.Bytes
import jonathanlocke.katalyst.nucleus.values.bytes.Bytes.Companion.bytes

/**
 * A serialization session contains the state of an ongoing serialization process. A [SerializationLimiter]
 * is used to determine whether or not the serialization should continue.
 *
 * @property processed The number of bytes processed so far
 *
 * @see SerializationLimiter
 */
open class SerializationSession {

    /**
     * The number of bytes processed so far
     */
    var processed = bytes(0)

    /**
     * Increments the number of bytes processed
     * @param bytes The number of bytes to increment by
     */
    fun processed(bytes: Bytes) {
        this.processed += bytes
    }
}
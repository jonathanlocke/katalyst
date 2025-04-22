package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.values.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.count

class SerializationSession {

    var lines = count(0)
    var bytes = bytes(0)
}
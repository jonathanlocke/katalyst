package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.problems.ProblemListener

/**
 * Serializes a value to a string
 */
interface Serializer<Value> {

    /**
     * Serializes a value to a string
     * @param listener A problem listener to report problems to
     * @param value The value to serialize
     * @return The serialized string
     */
    fun serialize(listener: ProblemListener, value: Value): String
}
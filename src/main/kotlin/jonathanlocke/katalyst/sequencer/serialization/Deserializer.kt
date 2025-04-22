package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.problems.ProblemListener

/**
 * Deserializes a string to a value
 *
 * @see Serialization
 * @see ProblemListener
 */
interface Deserializer<Value> {

    /**
     * Deserializes a string to a value
     * @param listener A problem listener to report problems to
     * @param text The string to deserialize
     * @return The deserialized value
     */
    fun deserialize(listener: ProblemListener, text: String): Value
}
package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.Throw

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
    fun serialize(value: Value, listener: ProblemListener = Throw()): String
}
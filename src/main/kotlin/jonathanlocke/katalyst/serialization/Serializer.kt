package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.ThrowOnError.Companion.throwOnError

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
    fun serialize(listener: ProblemListener = throwOnError, value: Value): String
}
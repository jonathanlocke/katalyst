package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.ThrowOnError.Companion.throwOnError

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
    fun deserialize(text: String, listener: ProblemListener = throwOnError): Value
}
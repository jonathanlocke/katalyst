package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ThrowOnError.Companion.throwOnError

/**
 * Deserializes a string to a value
 *
 * @see Serialization
 * @see ProblemHandler
 */
interface Deserializer<Value> {

    /**
     * Deserializes a string to a value
     * @param problemHandler A problem handler to report problems to
     * @param text The string to deserialize
     * @return The deserialized value
     */
    fun deserialize(text: String, problemHandler: ProblemHandler = throwOnError): Value
}
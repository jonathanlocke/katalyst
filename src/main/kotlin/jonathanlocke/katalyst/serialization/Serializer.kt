package jonathanlocke.katalyst.serialization

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.throwOnError

/**
 * Serializes a value to a string
 */
interface Serializer<Value> {

    /**
     * Serializes a value to a string
     * @param problemHandler A problem handler to report problems to
     * @param value The value to serialize
     * @return The serialized string
     */
    fun serialize(value: Value, problemHandler: ProblemHandler = throwOnError): String
}
package jonathanlocke.katalyst.checkpoint.validation

/**
 * A problem encountered during validation.
 *
 * @param message The message associated with the problem
 * @param value The value associated with the problem
 */
open class ValidationProblem<T>(val message: String, val value: T)

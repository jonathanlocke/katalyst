package jonathanlocke.katalyst.nucleus.problems.categories

import jonathanlocke.katalyst.nucleus.problems.Problem

/**
 * A failure that requires execution to terminate.
 *
 * @param message A message describing the failure
 * @param cause Any exception that caused the failure
 * @param value Any value associated with the failure
 *
 * @see Problem
 */
open class Failure(message: String, cause: Throwable? = null, value: Any? = null) : Problem(message, cause, value)
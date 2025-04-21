package jonathanlocke.katalyst.nucleus.language.problems.categories

import jonathanlocke.katalyst.nucleus.language.problems.Problem

/**
 * An error.
 *
 * @param message A message describing the error
 * @param cause Any exception that caused the error
 * @param value Any value associated with the error
 *
 * @see Problem
 */
open class Error(message: String, cause: Throwable? = null, value: Any? = null) : Problem(message, cause, value)
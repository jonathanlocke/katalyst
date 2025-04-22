package jonathanlocke.katalyst.nucleus.problems.categories

import jonathanlocke.katalyst.nucleus.problems.Problem

/**
 * A warning.
 *
 * @param message A message describing the warning
 * @param cause Any exception that caused the warning
 * @param value Any value associated with the warning
 *
 * @see Problem
 */
open class Warning(message: String, cause: Throwable? = null, value: Any? = null) : Problem(message, cause, value)
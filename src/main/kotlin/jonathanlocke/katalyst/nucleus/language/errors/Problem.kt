package jonathanlocke.katalyst.nucleus.language.errors

/**
 * Signifies a problem.
 *
 * @param message A message describing the problem
 * @param throwable Any exception that caused the problem
 * @param value Any value associated with the problem
 */
class Problem(message: String, throwable: Throwable? = null, value: Any? = null)

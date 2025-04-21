package jonathanlocke.katalyst.nucleus.language.problems

/**
 * Signifies a problem.
 *
 * @param message A message describing the problem
 * @param cause Any exception that caused the problem
 */
abstract class Problem(val message: String, val cause: Throwable? = null, val value: Any? = null)
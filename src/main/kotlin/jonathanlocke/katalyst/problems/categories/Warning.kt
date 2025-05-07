package jonathanlocke.katalyst.problems.categories

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.Problem.Effect.CONTINUE

/**
 * A warning.
 *
 * @param message A message describing the warning
 * @param cause Any exception that caused the warning
 * @param value Any value associated with the warning
 *
 * @see Problem
 */
open class Warning(message: String, cause: Throwable? = null, value: Any? = null) : Problem(message, cause, value) {
    override val effect = CONTINUE
    override fun prefixed(prefix: String): Problem = Failure(prefix + message, cause, value)
}
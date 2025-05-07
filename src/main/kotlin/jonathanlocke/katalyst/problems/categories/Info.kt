package jonathanlocke.katalyst.problems.categories

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.Problem.Effect.CONTINUE

/**
 * An informational message
 *
 * @param message A message
 * @see Problem
 */
open class Info(message: String) : Problem(message) {
    override val effect = CONTINUE
    override fun prefixed(prefix: String): Problem = Info(prefix + message)
}
package jonathanlocke.katalyst.problems.categories

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.Problem.Effect.CONTINUE

/**
 * A trace message for debugging.
 *
 * @param message A message
 * @see Problem
 */
open class Trace(message: String) : Problem(message) {
    override val effect = CONTINUE
}
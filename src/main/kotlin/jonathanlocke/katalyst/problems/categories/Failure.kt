package jonathanlocke.katalyst.problems.categories

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.Problem.Effect.STOP

/**
 * A failure that requires execution to terminate.
 *
 * @param message A message describing the failure
 * @param cause Any exception that caused the failure
 * @param value Any value associated with the failure
 *
 * @see Problem
 */
open class Failure(message: String, cause: Throwable? = null, value: Any? = null) : Problem(message, cause, value) {
    override val effect = STOP
}
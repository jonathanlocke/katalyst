package jonathanlocke.katalyst.validation.problems

import jonathanlocke.katalyst.problems.Problem.Effect.STOP
import jonathanlocke.katalyst.problems.categories.Error

class ValidationError(message: String, cause: Throwable? = null, value: Any? = null) : Error(message, cause, value) {
    override val effect = STOP
}
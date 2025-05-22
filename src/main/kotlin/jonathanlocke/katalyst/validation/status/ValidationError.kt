package jonathanlocke.katalyst.validation.status

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.STOP
import jonathanlocke.katalyst.status.categories.Error

class ValidationError(message: String, cause: Throwable? = null, value: Any? = null) : Error(message, cause, value) {
    override val effect = STOP
    override fun prefixed(prefix: String): Status = ValidationError(prefix + message, cause, value)
}

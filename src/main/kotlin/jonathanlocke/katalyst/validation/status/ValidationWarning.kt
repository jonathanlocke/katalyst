package jonathanlocke.katalyst.validation.status

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.categories.Warning

class ValidationWarning(message: String, cause: Throwable? = null, value: Any? = null) :
    Warning(message, cause, value) {
    override val effect = Effect.CONTINUE
    override fun prefixed(prefix: String): Status = ValidationWarning(prefix + message, cause, value)
}

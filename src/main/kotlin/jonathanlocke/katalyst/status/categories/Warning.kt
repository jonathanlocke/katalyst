package jonathanlocke.katalyst.status.categories

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.CONTINUE

/**
 * A warning.
 *
 * @param message A message describing the warning
 * @param cause Any exception that caused the warning
 * @param value Any value associated with the warning
 *
 * @see Status
 */
open class Warning(message: String, cause: Throwable? = null, value: Any? = null) : Status(message, cause, value) {
    override val effect = CONTINUE
    override fun prefixed(prefix: String): Status = Failure(prefix + message, cause, value)
}

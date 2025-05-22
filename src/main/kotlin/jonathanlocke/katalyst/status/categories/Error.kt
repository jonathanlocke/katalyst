package jonathanlocke.katalyst.status.categories

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.STOP

/**
 * An error.
 *
 * @param message A message describing the error
 * @param cause Any exception that caused the error
 * @param value Any value associated with the error
 *
 * @see Status
 */
open class Error(message: String, cause: Throwable? = null, value: Any? = null) : Status(message, cause, value) {
    override val effect = STOP
    override fun prefixed(prefix: String): Status = Error(prefix + message, cause, value)
}

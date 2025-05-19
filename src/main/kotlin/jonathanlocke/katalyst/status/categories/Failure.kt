package jonathanlocke.katalyst.status.categories

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.STOP

/**
 * A failure that requires execution to terminate.
 *
 * @param message A message describing the failure
 * @param cause Any exception that caused the failure
 * @param value Any value associated with the failure
 *
 * @see Status
 */
open class Failure(message: String, cause: Throwable? = null, value: Any? = null) : Status(message, cause, value) {
    override val effect = STOP
    override fun prefixed(prefix: String): Status = Failure(prefix + message, cause, value)
}
package jonathanlocke.katalyst.status.categories

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.CONTINUE

/**
 * A trace message for debugging.
 *
 * @param message A message
 * @see Status
 */
open class Trace(message: String) : Status(message) {
    override val effect = CONTINUE
    override fun prefixed(prefix: String): Status = Trace(prefix + message)
}
package jonathanlocke.katalyst.status.categories

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.CONTINUE

/**
 * An informational message
 *
 * @param message A message
 * @see Status
 */
open class Info(message: String) : Status(message) {
    override val effect = CONTINUE
    override fun prefixed(prefix: String): Status = Info(prefix + message)
}

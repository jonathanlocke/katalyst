package jonathanlocke.katalyst.status.handlers

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.Status.Effect.STOP
import jonathanlocke.katalyst.status.StatusException
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerBase

/**
 * [StatusHandler] that throws an exception.
 *
 * ```
 * text.convert(ToInt(), throwOnError))
 * ```
 *
 * Note: throwOnError is the default in most cases, as in this case.
 *
 * @see StatusHandler
 * @see StatusHandlerBase
 * @see StatusException
 * @see Status
 */
class ThrowOnError : StatusHandlerBase() {

    override fun onHandle(status: Status) {
        if (status.effect == STOP) throw StatusException("Halting execution:", statuses())
    }
}

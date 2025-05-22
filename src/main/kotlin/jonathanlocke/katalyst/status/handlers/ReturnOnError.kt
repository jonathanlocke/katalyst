package jonathanlocke.katalyst.status.handlers

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerBase
import jonathanlocke.katalyst.status.categories.Failure

/**
 * [StatusHandler] that allows the caller to proceed and return some value (or no value), *unless* a [Failure] is
 * encountered. When a failure is received, this handler will throw an exception.
 *
 * ```
 * text.convert(ToInt(), returnOnError)
 * ```
 *
 * @see StatusHandler
 * @see StatusHandlerBase
 * @see Status
 */
class ReturnOnError : StatusHandlerBase() {

    override fun onHandle(status: Status) = true
}

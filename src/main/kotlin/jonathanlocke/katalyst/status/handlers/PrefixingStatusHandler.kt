package jonathanlocke.katalyst.status.handlers

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerBase

class PrefixingStatusHandler(
    private val prefix: String,
    private val statusHandler: StatusHandler,
) : StatusHandlerBase() {

    companion object {
        fun StatusHandler.prefixedWith(prefix: String) = PrefixingStatusHandler(prefix, this)
    }

    override fun onHandle(status: Status) {
        statusHandler.handle(status.prefixed(prefix))
    }
}
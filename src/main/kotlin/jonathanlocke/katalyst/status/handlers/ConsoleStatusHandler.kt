package jonathanlocke.katalyst.status.handlers

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.StatusHandlerBase

class ConsoleStatusHandler : StatusHandlerBase() {
    override fun onHandle(status: Status) {
        println(status)
    }
}
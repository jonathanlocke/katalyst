package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.status.handlers.ConsoleStatusHandler
import jonathanlocke.katalyst.status.handlers.ReturnOnError
import jonathanlocke.katalyst.status.handlers.ThrowOnError

class StatusHandlers {

    companion object {
        val returnOnError = ReturnOnError()
        val throwOnError = ThrowOnError()
        val consoleStatusHandler = ConsoleStatusHandler()
    }
}
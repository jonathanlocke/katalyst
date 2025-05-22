package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.status.handlers.ConsoleStatusHandler
import jonathanlocke.katalyst.status.handlers.ReturnOnError
import jonathanlocke.katalyst.status.handlers.ThrowOnError

object StatusHandlers {

    val returnOnError = ReturnOnError()
    val throwOnError = ThrowOnError()
    val consoleStatusHandler = ConsoleStatusHandler()
}

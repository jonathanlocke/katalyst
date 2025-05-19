package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.status.StatusHandler

interface Logger : StatusHandler {
    fun logs(): List<Log>
}

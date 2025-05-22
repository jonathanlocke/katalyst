package jonathanlocke.katalyst.logtest

import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.logging.Log
import jonathanlocke.katalyst.logging.Logger
import jonathanlocke.katalyst.logging.LoggerMixin
import jonathanlocke.katalyst.logging.loggers.contextual.CodeContextLogger
import jonathanlocke.katalyst.logging.logs.memory.MemoryLog
import jonathanlocke.katalyst.status.categories.Error
import jonathanlocke.katalyst.status.categories.Failure
import jonathanlocke.katalyst.status.categories.Warning
import kotlin.test.Test
import kotlin.test.assertEquals

class LoggingTest : LoggerMixin {

    val memoryLog = MemoryLog()

    override fun logger(): Logger = CodeContextLogger(listOf(memoryLog))
    override fun logs(): List<Log> = logger().logs()

    @Test
    fun test() {
        warning("warning")
        error("this is a test of the emergency broadcast system")
        assertEquals(count(1), memoryLog.statistics(Warning::class.java))
        assertEquals(count(1), memoryLog.statistics(Error::class.java))
        assertEquals(count(0), memoryLog.statistics(Failure::class.java))
    }
}
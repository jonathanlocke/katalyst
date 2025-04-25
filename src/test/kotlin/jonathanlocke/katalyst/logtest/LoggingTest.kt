package jonathanlocke.katalyst.logtest

import jonathanlocke.katalyst.logging.LoggerMixin
import kotlin.test.Test

class LoggingTest : LoggerMixin {

//    val memoryLog = MemoryLog()
//
//    override fun logger(): Logger = ContextualLogger(listOf(memoryLog))
//    override fun logs(): List<Log> = listOf(memoryLog)
//    override val problems: MutableList<Problem>
//        get() = TODO("Not yet implemented")

    @Test
    fun test() {
        warning("warning")
        error("this is a test of the emergency broadcast system")
        Thread.sleep(1000)
    }
}
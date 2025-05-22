package jonathanlocke.katalyst.logging.loggers.contextual

import jonathanlocke.katalyst.logging.Log
import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.logging.Logger
import jonathanlocke.katalyst.logging.loggers.contextual.CodeContext.Companion.codeContext
import jonathanlocke.katalyst.status.NullStatusList
import jonathanlocke.katalyst.status.Status
import java.lang.Thread.currentThread
import java.time.Duration.between
import java.time.Instant
import java.util.function.Predicate

class CodeContextLogger(val logs: List<Log>) : Logger {

    val codeContext = codeContext()
    val filters = mutableListOf<Predicate<LogEntry>>()
    val created = Instant.now()

    override fun statuses() = NullStatusList()
    override fun handle(status: Status) = addToLogs(codeContext, currentThread(), status)
    override fun logs(): List<Log> = logs

    private fun addToLogs(codeContext: CodeContext, thread: Thread, status: Status): Boolean {
        val now = Instant.now()
        val entry = LogEntry(now, between(created, now), thread, codeContext, status)
        if (shouldLog(entry)) {
            for (log in logs()) {
                log.log(entry)
            }
        }
        return true
    }

    fun shouldLog(entry: LogEntry): Boolean = filters.all { it.test(entry) }
}

package jonathanlocke.katalyst.logging.loggers.contextual

import jonathanlocke.katalyst.logging.Log
import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.logging.Logger
import jonathanlocke.katalyst.logging.loggers.contextual.CodeLocation.Companion.codeLocation
import jonathanlocke.katalyst.problems.Problem
import java.lang.Thread.currentThread
import java.time.Duration.between
import java.time.Instant
import java.util.function.Predicate

class ContextualLogger(val logs: List<Log>) : Logger {

    val codeLocation = codeLocation()
    val filters = mutableListOf<Predicate<LogEntry>>()
    val created = Instant.now()

    override fun problems() = NullProblemList()

    override fun receive(problem: Problem) = addToLogs(codeLocation, currentThread(), problem)
    override fun logs(): List<Log> = logs

    private fun addToLogs(location: CodeLocation, thread: Thread, problem: Problem) {
        val now = Instant.now()
        val entry = LogEntry(now, between(created, now), thread, location, problem)
        if (shouldLog(entry)) {
            for (log in logs()) {
                log.receive(entry)
            }
        }
    }

    fun shouldLog(entry: LogEntry): Boolean = filters.all { it.test(entry) }
}

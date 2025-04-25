package jonathanlocke.katalyst.logging.loggers.contextual

import jonathanlocke.katalyst.logging.Log
import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.logging.Logger
import jonathanlocke.katalyst.problems.Problem
import java.lang.Thread.currentThread
import java.time.Instant
import java.util.function.Predicate

abstract class ContextualLogger : Logger {

    val codeLocation = CodeLocation.Companion.codeContext()
    val filters = mutableListOf<Predicate<LogEntry>>()

    override val problems = NullProblemList()

    override fun receive(problem: Problem) = addToLogs(codeLocation, currentThread(), problem)
    abstract override fun logs(): List<Log>

    private fun addToLogs(context: CodeLocation, thread: Thread, problem: Problem) {
        val entry = LogEntry(context, thread, Instant.now(), problem)
        if (shouldLog(entry)) {
            for (log in logs()) {
                log.receive(entry)
            }
        }
    }

    fun shouldLog(entry: LogEntry): Boolean = filters.all { it.test(entry) }
}

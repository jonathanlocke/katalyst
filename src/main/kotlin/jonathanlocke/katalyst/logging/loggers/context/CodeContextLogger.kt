package jonathanlocke.katalyst.logging.loggers.context

import jonathanlocke.katalyst.logging.Log
import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.logging.Logger
import jonathanlocke.katalyst.problems.Problem
import java.lang.Thread.currentThread
import java.time.Instant
import java.util.function.Predicate

abstract class CodeContextLogger : Logger {

    val codeContext = CodeContext.Companion.codeContext()
    val filters = mutableListOf<Predicate<LogEntry>>()

    override val problems = NullProblemList()

    override fun receive(problem: Problem) = addToLogs(codeContext, currentThread(), problem)
    abstract override fun logs(): List<Log>

    private fun addToLogs(context: CodeContext, thread: Thread, problem: Problem) {
        val entry = LogEntry(context, thread, Instant.now(), problem)
        if (shouldLog(entry)) {
            for (log in logs()) {
                log.receive(entry)
            }
        }
    }

    fun shouldLog(entry: LogEntry): Boolean = filters.all { it.test(entry) }
}

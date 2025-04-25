package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.loggers.context.CodeContext
import jonathanlocke.katalyst.problems.Problem
import java.time.Instant

class LogEntry(
    val context: CodeContext,
    val thread: Thread,
    val created: Instant,
    val problem: Problem,
)
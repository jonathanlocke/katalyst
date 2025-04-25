package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.loggers.contextual.CodeLocation
import jonathanlocke.katalyst.problems.Problem
import java.time.Instant

class LogEntry(
    val context: CodeLocation,
    val thread: Thread,
    val created: Instant,
    val problem: Problem,
)
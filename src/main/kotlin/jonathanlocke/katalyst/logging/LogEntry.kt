package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.loggers.contextual.CodeLocation
import jonathanlocke.katalyst.problems.Problem
import java.time.Duration
import java.time.Instant

class LogEntry(
    val created: Instant,
    val elapsed: Duration,
    val thread: Thread,
    val location: CodeLocation,
    val problem: Problem,
)
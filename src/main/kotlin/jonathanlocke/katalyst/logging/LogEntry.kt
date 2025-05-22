package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.loggers.contextual.CodeContext
import jonathanlocke.katalyst.status.Status
import java.time.Duration
import java.time.Instant

class LogEntry(
    val created: Instant,
    val elapsed: Duration,
    val thread: Thread,
    val codeContext: CodeContext,
    val status: Status,
)

package jonathanlocke.katalyst.data.values.temporal

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ConsoleProblemHandler
import jonathanlocke.katalyst.text.formatting.formatters.time.TimeFormatters.Companion.durationFormatter
import java.time.Duration
import java.time.Instant

class TimeExtensions {

    companion object {

        fun Instant.elapsedSince() = Duration.between(Instant.now(), this)
        fun Duration.format() = durationFormatter.format(this)

        fun <Value> profile(problemHandler: ProblemHandler = ConsoleProblemHandler(), operation: () -> Value): Value {
            val start = Instant.now()
            problemHandler.info("Operation started")
            val value = operation.invoke()
            problemHandler.info("Operation completed in ${start.elapsedSince().format()}")
            return value
        }
    }
}
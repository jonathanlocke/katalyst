package jonathanlocke.katalyst.data.values.temporal

import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.handlers.ConsoleStatusHandler
import jonathanlocke.katalyst.text.formatting.formatters.time.TimeFormatters.Companion.durationFormatter
import java.time.Duration
import java.time.Instant

class TimeExtensions {

    companion object {

        fun Instant.elapsedSince() = Duration.between(Instant.now(), this)
        fun Duration.format() = durationFormatter.format(this)

        fun <Value> profile(statusHandler: StatusHandler = ConsoleStatusHandler(), operation: () -> Value): Value {
            val start = Instant.now()
            statusHandler.info("Operation started")
            val value = operation.invoke()
            statusHandler.info("Operation completed in ${start.elapsedSince().format()}")
            return value
        }
    }
}
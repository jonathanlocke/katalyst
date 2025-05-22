package jonathanlocke.katalyst.text.formatting.formatters.time

import jonathanlocke.katalyst.text.formatting.Formatter
import java.time.Duration

class DurationFormatter : Formatter<Duration> {

    companion object {
        private const val SECONDS_PER_MINUTE = 60
        private const val MINUTES_PER_HOUR = 60
    }

    override fun format(duration: Duration): String {
        val seconds = duration.seconds
        val minutes = seconds / SECONDS_PER_MINUTE
        val hours = minutes / MINUTES_PER_HOUR
        return when {
            hours > 0 -> String.format("%.1f hours", hours + (minutes % MINUTES_PER_HOUR) / MINUTES_PER_HOUR.toDouble())
            minutes > 0 -> String.format(
                "%.1f minutes",
                minutes + (seconds % SECONDS_PER_MINUTE) / SECONDS_PER_MINUTE.toDouble()
            )

            else -> String.format("%d seconds", seconds)
        }
    }
}

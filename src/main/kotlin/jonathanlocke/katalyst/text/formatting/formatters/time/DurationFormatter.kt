package jonathanlocke.katalyst.text.formatting.formatters.time

import jonathanlocke.katalyst.text.formatting.Formatter
import java.time.Duration

class DurationFormatter : Formatter<Duration> {
    override fun format(duration: Duration): String {
        val seconds = duration.seconds
        val minutes = seconds / 60
        val hours = minutes / 60
        return when {
            hours > 0 -> String.format("%.1f hours", hours + (minutes % 60) / 60.0)
            minutes > 0 -> String.format("%.1f minutes", minutes + (seconds % 60) / 60.0)
            else -> String.format("%d seconds", seconds)
        }
    }
}
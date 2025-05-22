package jonathanlocke.katalyst.text.formatting.formatters.time

import jonathanlocke.katalyst.text.formatting.Formatter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeFormatter : Formatter<Instant> {

    private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-h.mm.ssa")

    override fun format(duration: Instant): String {
        return duration.atZone(ZoneId.systemDefault()).format(formatter).lowercase()
    }
}

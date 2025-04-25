package jonathanlocke.katalyst.logging.logs.text.formatting

import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.text.formatting.Formatter

class BasicLogEntryFormatter : Formatter<LogEntry> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun format(entry: LogEntry): String {
        return "${entry.created} ${entry.thread.name} ${entry.location} ${entry.problem}"
    }
}
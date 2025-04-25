package jonathanlocke.katalyst.logging.logs.text.formatting

import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter

class ColumnarLogEntryFormatter(
    override val columns: List<Column>,
    private val fieldsLambda: (LogEntry) -> List<String>
) : ColumnarFormatter<LogEntry>() {
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun fields(entry: LogEntry): List<String> {
        return fieldsLambda(entry)
    }
}
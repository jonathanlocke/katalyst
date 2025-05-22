package jonathanlocke.katalyst.logging.logs.text.formatting

import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter

class ColumnarLogEntryFormatter(
    override val columns: List<Column>,
    private val fieldsLambda: (LogEntry) -> List<String>,
) : ColumnarFormatter<LogEntry>() {
    override fun fields(value: LogEntry): List<String> {
        return fieldsLambda(value)
    }
}

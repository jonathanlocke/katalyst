package jonathanlocke.katalyst.logging.logs.text.console

import jonathanlocke.katalyst.logging.LogBase
import jonathanlocke.katalyst.logging.LogEntry
import jonathanlocke.katalyst.text.formatting.Formatter
import java.io.Console

class ConsoleLog(val formatter: Formatter<LogEntry>) : LogBase() {

    private val console: Console = System.console()

    override fun onReceive(entry: LogEntry) {
        console.println(formatter.format(entry))
    }
}
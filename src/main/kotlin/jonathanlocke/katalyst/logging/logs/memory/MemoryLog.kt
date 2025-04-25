package jonathanlocke.katalyst.logging.logs.memory

import jonathanlocke.katalyst.logging.LogBase
import jonathanlocke.katalyst.logging.LogEntry

class MemoryLog : LogBase() {
    val entries = mutableListOf<LogEntry>()
    override fun onReceive(entry: LogEntry) {
        entries.add(entry)
    }
}
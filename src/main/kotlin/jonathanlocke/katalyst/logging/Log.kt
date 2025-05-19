package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.status.Status

interface Log {

    enum class Mode {
        SYNCHRONOUS,
        ASYNCHRONOUS
    }

    var mode: Mode
    fun receive(entry: LogEntry)
    fun statuses(): Map<Class<out Status>, Count>

    @Suppress("UNCHECKED_CAST")
    fun statuses(type: Class<out Status>): Count = statuses()[type] ?: count(0)
}

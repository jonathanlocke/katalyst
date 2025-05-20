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
    fun log(entry: LogEntry)

    fun statistics(): Map<Class<out Status>, Count>
    fun statistics(type: Class<out Status>): Count = statistics()[type] ?: count(0)
}

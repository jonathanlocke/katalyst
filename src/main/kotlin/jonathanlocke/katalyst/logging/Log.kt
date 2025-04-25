package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.Problem

interface Log {

    enum class Mode {
        SYNCHRONOUS,
        ASYNCHRONOUS
    }

    var mode: Mode
    fun receive(entry: LogEntry)
    fun problems(): Map<Class<out Problem>, Count>

    @Suppress("UNCHECKED_CAST")
    fun problems(type: Class<out Problem>): Count = problems()[type] ?: count(0)
}

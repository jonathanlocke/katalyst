package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import kotlin.reflect.KClass

interface Log {

    enum class Mode {
        SYNCHRONOUS,
        ASYNCHRONOUS
    }

    var mode: Mode
    fun receive(entry: LogEntry)
    fun problems(): Map<ValueType<Problem>, Count>

    @Suppress("UNCHECKED_CAST")
    fun <T : Problem> problems(type: KClass<T>): Count =
        problems()[valueType(type) as ValueType<Problem>] ?: count(0)
}

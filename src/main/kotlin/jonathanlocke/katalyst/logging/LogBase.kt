package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.logging.Log.Mode.ASYNCHRONOUS
import jonathanlocke.katalyst.logging.Log.Mode.SYNCHRONOUS
import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.reflection.ValueType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

abstract class LogBase : Log {

    private val problemCounts = ConcurrentHashMap<ValueType<Problem>, AtomicInteger>()
    private val logEntryQueue = LogEntryQueue(this)

    override var mode: Log.Mode = SYNCHRONOUS
        set(value) {
            if (field != value) {
                field = value
                when (value) {
                    ASYNCHRONOUS -> logEntryQueue.start()
                    SYNCHRONOUS -> logEntryQueue.stop()
                }
            }
        }

    init {
        if (mode == ASYNCHRONOUS) {
            logEntryQueue.start()
        }
    }

    override fun problems(): Map<ValueType<Problem>, Count> = problemCounts
        .mapValues { (_, count) -> count(count.get()) }

    override fun receive(entry: LogEntry) {
        @Suppress("UNCHECKED_CAST")
        problemCounts
            .computeIfAbsent(entry.problem.type as ValueType<Problem>) { AtomicInteger() }
            .incrementAndGet()

        when (mode) {
            SYNCHRONOUS -> onReceive(entry)
            ASYNCHRONOUS -> logEntryQueue.offer(entry)
        }
    }

    abstract fun onReceive(entry: LogEntry)
}

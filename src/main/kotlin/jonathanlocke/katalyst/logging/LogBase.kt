package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.logging.Log.Mode.ASYNCHRONOUS
import jonathanlocke.katalyst.logging.Log.Mode.SYNCHRONOUS
import jonathanlocke.katalyst.status.Status
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

abstract class LogBase : Log {

    private val statusCounts = ConcurrentHashMap<Class<out Status>, AtomicInteger>()
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

    override fun statuses(): Map<Class<out Status>, Count> = statusCounts
        .mapValues { (_, count) -> count(count.get()) }

    override fun receive(entry: LogEntry) {
        statusCounts
            .computeIfAbsent(entry.status::class.java) { AtomicInteger() }
            .incrementAndGet()

        when (mode) {
            SYNCHRONOUS -> onReceive(entry)
            ASYNCHRONOUS -> logEntryQueue.offer(entry)
        }
    }

    abstract fun onReceive(entry: LogEntry)
}

package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.Log.Mode.ASYNCHRONOUS
import jonathanlocke.katalyst.logging.Log.Mode.SYNCHRONOUS

abstract class LogBase : Log {

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

    override fun receive(entry: LogEntry) {
        when (mode) {
            SYNCHRONOUS -> onReceive(entry)
            ASYNCHRONOUS -> logEntryQueue.offer(entry)
        }
    }

    abstract fun onReceive(entry: LogEntry)
}

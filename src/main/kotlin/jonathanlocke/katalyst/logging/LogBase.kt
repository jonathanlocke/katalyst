package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.LogBase.LogMode.ASYNCHRONOUS
import java.util.concurrent.ArrayBlockingQueue

abstract class LogBase protected constructor(private val mode: LogMode = ASYNCHRONOUS) : Log {

    enum class LogMode {
        SYNCHRONOUS,
        ASYNCHRONOUS
    }

    private val queue: ArrayBlockingQueue<LogEntry> by lazy { ArrayBlockingQueue<LogEntry>(2048) }

    init {
        if (mode == ASYNCHRONOUS) {
            Thread {
                while (true) {
                    onReceive(queue.take())
                }
            }.start()
        }
    }

    override fun receive(entry: LogEntry) {
        when (mode) {
            LogMode.SYNCHRONOUS -> onReceive(entry)
            ASYNCHRONOUS -> queue.offer(entry)
        }
    }

    abstract fun onReceive(entry: LogEntry)
}

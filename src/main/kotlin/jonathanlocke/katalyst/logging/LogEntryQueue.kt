package jonathanlocke.katalyst.logging

import java.util.concurrent.ArrayBlockingQueue

internal class LogEntryQueue(private val logBase: LogBase) {

    private val queue: ArrayBlockingQueue<LogEntry> = ArrayBlockingQueue(2048)
    private var worker: Thread? = null

    fun start() {
        worker = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    logBase.onLog(queue.take())
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }.apply { start() }
    }

    fun stop() {
        worker?.interrupt()
        worker?.join()
        worker = null
    }

    fun offer(entry: LogEntry) {
        queue.offer(entry)
    }
}

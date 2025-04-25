package jonathanlocke.katalyst.logging

interface Log {

    enum class Mode {
        SYNCHRONOUS,
        ASYNCHRONOUS
    }

    var mode: Mode
    fun receive(entry: LogEntry)
}

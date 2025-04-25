package jonathanlocke.katalyst.logging

interface Log {
    fun receive(entry: LogEntry)
}

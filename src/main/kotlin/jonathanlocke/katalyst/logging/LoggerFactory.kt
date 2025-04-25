package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.loggers.contextual.ContextualLogger
import jonathanlocke.katalyst.logging.logs.text.ColumnarFormatter
import jonathanlocke.katalyst.logging.logs.text.ColumnarFormatter.Type.FIXED_WIDTH
import jonathanlocke.katalyst.logging.logs.text.ColumnarFormatter.Type.VARIABLE_WIDTH
import jonathanlocke.katalyst.logging.logs.text.console.ConsoleLog

interface LoggerFactory {

    fun newLogger(): Logger

    companion object {

        val consoleLoggerFactory: LoggerFactory = object : LoggerFactory {
            override fun newLogger(): Logger = object : ContextualLogger() {
                val formatter = object : ColumnarFormatter<LogEntry>(
                    listOf(
                        Column("Time", 20, FIXED_WIDTH),
                        Column("Thread", 10, FIXED_WIDTH),
                        Column("Class", 20, VARIABLE_WIDTH, 40),
                        Column("Method", 20, FIXED_WIDTH, 40),
                        Column("Line", 5, FIXED_WIDTH),
                        Column("Message", 100, VARIABLE_WIDTH),
                    )
                ) {
                    override fun fields(entry: LogEntry): List<String> {
                        return listOf(
                            entry.created.toString(),
                            entry.thread.name,
                            entry.context.type.simpleName,
                            entry.context.methodName,
                            entry.context.lineNumber.toString(),
                            entry.problem.message
                        )
                    }
                }
                val consoleLog = ConsoleLog(formatter)
                override fun logs(): List<Log> = listOf(consoleLog)
            }
        }

        var defaultLoggerFactory: LoggerFactory = consoleLoggerFactory
    }
}
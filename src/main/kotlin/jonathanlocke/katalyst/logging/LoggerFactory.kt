package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.loggers.contextual.CodeContextLogger
import jonathanlocke.katalyst.logging.logs.text.console.ConsoleLog
import jonathanlocke.katalyst.logging.logs.text.formatting.ColumnarLogEntryFormatter
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter.Column
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter.Justify.LEFT
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter.Justify.RIGHT
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter.Type.FIXED_WIDTH
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter.Type.VARIABLE_WIDTH
import jonathanlocke.katalyst.text.formatting.formatters.time.TimeFormatters.durationFormatter
import jonathanlocke.katalyst.text.formatting.formatters.time.TimeFormatters.timeFormatter

interface LoggerFactory {

    fun newLogger(): Logger

    companion object {

        val consoleLog = ConsoleLog(
            ColumnarLogEntryFormatter(
                listOf(
                    Column("Time", 20, FIXED_WIDTH, RIGHT),
                    Column("Elapsed", 1, VARIABLE_WIDTH, LEFT, 20),
                    Column(name = "Type", 1, VARIABLE_WIDTH, RIGHT, 24),
                    Column("Thread", 1, VARIABLE_WIDTH, LEFT, 32),
                    Column("Code Context", 1, VARIABLE_WIDTH, RIGHT, 64),
                    Column("Message", 1, VARIABLE_WIDTH, LEFT),
                )
            ) { entry ->
                listOf(
                    timeFormatter.format(entry.created),
                    durationFormatter.format(entry.elapsed),
                    entry.status::class.simpleName ?: "Unknown",
                    entry.thread.name,
                    entry.codeContext.toString(),
                    entry.status.message
                )
            })

        val consoleLoggerFactory: LoggerFactory = object : LoggerFactory {
            override fun newLogger(): Logger = CodeContextLogger(listOf(consoleLog))
        }

        var defaultLoggerFactory: LoggerFactory = consoleLoggerFactory
    }
}

package jonathanlocke.katalyst.resources.streams

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.throwOnError
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.streams.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.serialization.Deserializer
import jonathanlocke.katalyst.serialization.Serializer
import java.io.*

class Streamer(val streamable: Streamable) {

    fun withReader(code: (BufferedReader) -> Unit) {
        streamable.openForReading().use { input ->
            InputStreamReader(input).buffered().use { reader ->
                code.invoke(reader)
            }
        }
    }

    fun withInput(code: (InputStream) -> Unit) = streamable.openForReading().use { input ->
        code.invoke(input.buffered())
    }

    fun withWriter(mode: WriteMode = DoNotOverwrite, code: (BufferedWriter) -> Unit) {
        streamable.openForWriting(mode).use { output ->
            OutputStreamWriter(output).buffered().use { writer ->
                code.invoke(writer)
            }
        }
    }

    fun withOutput(mode: WriteMode = DoNotOverwrite, code: (OutputStream) -> Unit) =
        streamable.openForWriting(mode).use { output ->
            code.invoke(output.buffered())
        }

    fun <Value> serialize(serializer: Serializer<Value>, value: Value, problemHandler: ProblemHandler = throwOnError) =
        withWriter { writer -> writer.write(serializer.serialize(problemHandler, value)) }

    fun <Value> deserialize(deserializer: Deserializer<Value>, problemHandler: ProblemHandler = throwOnError) =
        withReader { reader -> deserializer.deserialize(reader.readText(), problemHandler) }


    fun readBytes(): ByteArray = streamable.openForReading().use {
        return it.readBytes()
    }

    fun readLines(
        progressReporter: ProgressReporter = nullProgressReporter, receiver: (lineNumber: Int, text: String) -> Unit
    ) {
        withReader { reader ->
            var lineNumber = 1
            reader.forEachLine { line ->
                receiver(lineNumber++, line)
                progressReporter.next()
            }
        }
    }

    fun readLines(
        progressReporter: ProgressReporter = nullProgressReporter, receiver: (text: String) -> Unit
    ) {
        return readLines(progressReporter) { _, text -> receiver(text) }
    }

    fun readLines(progressReporter: ProgressReporter): List<String> = mutableListOf<String>().apply {
        readLines(progressReporter) { _, text -> add(text) }
    }

    fun readText() = streamable.openForReading().use { it.readBytes().decodeToString() }

    fun writeBytes(bytes: ByteArray) = withOutput { output ->
        output.write(bytes)
    }

    fun writeText(text: String) = withWriter { writer ->
        writer.write(text)
    }

    fun writeLines(lines: List<String>) = withWriter { writer ->
        lines.forEach { line ->
            writer.write(line)
            writer.newLine()
        }
    }
}
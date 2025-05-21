package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.location.path.Filename.Companion.parseFilename
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod.Copy
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.Overwrite
import jonathanlocke.katalyst.serialization.Deserializer
import jonathanlocke.katalyst.serialization.Serializer
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers.Companion.throwOnError
import java.io.*

class ResourceStreamer(
    val resourceStreamable: ResourceStreamable,
    val statusHandler: StatusHandler = throwOnError,
    val progressReporter: ProgressReporter = nullProgressReporter,
) : StatusHandler by statusHandler, ProgressReporter by progressReporter {

    fun copyTo(
        to: Resource,
        method: CopyMethod,
        mode: WriteMode,
    ) {
        requireOrFail(to.isWritable(), "Target is not writable: $to")
        resourceStreamable.openForReading().use { input ->
            when (method) {

                CopyAndRename -> {
                    val temporary = to.store.temporaryResourceLocation(parseFilename("copy"))
                    temporary.openForWriting(Overwrite, progressReporter).use { output ->
                        input.copyTo(output)
                    }
                    temporary.moveTo(to)
                }

                Copy -> {
                    to.openForWriting(Overwrite, progressReporter).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    fun <Value> withReader(code: (Reader) -> Value) =
        tryValue("Reading from text reader failed: $resourceStreamable") {
            resourceStreamable.openForReading().use { input ->
                InputStreamReader(input).buffered().use { reader ->
                    code.invoke(reader)
                }
            }
        }

    fun <Value> withWriter(mode: WriteMode = DoNotOverwrite, code: (Writer) -> Value) =
        tryValue("Writing to text writer failed: $resourceStreamable") {
            resourceStreamable.openForWriting(mode, progressReporter).use { output ->
                OutputStreamWriter(output).buffered().use { writer ->
                    code.invoke(writer)
                }
            }
        }

    fun <Value> withInput(code: (InputStream) -> Value) =
        tryValue("Reading from input stream failed: $resourceStreamable") {
            resourceStreamable.openForReading().use { input ->
                code.invoke(input.buffered())
            }
        }

    fun <Value> withOutput(mode: WriteMode = DoNotOverwrite, code: (ResourceOutputStream) -> Value) =
        tryValue("Writing to output stream failed: $resourceStreamable") {
            resourceStreamable.openForWriting(mode, progressReporter).use { output ->
                code.invoke(output)
            }
        }

    fun <Value> serialize(serializer: Serializer<Value>, value: Value) =
        withWriter { writer -> writer.write(serializer.serialize(value)) }

    fun <Value> deserialize(deserializer: Deserializer<Value>) =
        withReader { reader -> deserializer.deserialize(reader.readText()) }

    fun readText() = tryValue("Could not read text from $resourceStreamable") {
        resourceStreamable.openForReading().use { it.readBytes().decodeToString() }
    }

    fun writeText(text: String) = withWriter { writer ->
        writer.write(text)
    }

    fun writeBytes(bytes: ByteArray) = withOutput { output ->
        output.write(bytes)
    }

    fun readBytes(): ByteArray? {
        return tryValue("Could not read bytes from $resourceStreamable") {
            resourceStreamable.openForReading().use {
                it.readBytes()
            }
        }
    }

    fun readLines(
        receiver: (lineNumber: Int, text: String) -> Unit,
    ) = withReader { reader ->
        var lineNumber = 1
        reader.forEachLine { line ->
            receiver(lineNumber++, line)
            progressReporter.next()
        }
    }

    fun readLines(
        receiver: (text: String) -> Unit,
    ) = readLines { _, text -> receiver(text) }

    fun readLines(): List<String> = mutableListOf<String>().apply {
        readLines { _, text -> add(text) }
    }

    fun writeLines(lines: Iterable<String>) = withWriter { writer ->
        lines.forEach(writer::write)
    }
}
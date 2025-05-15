package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import java.io.InputStream

class ResourceInputStream(
    val progressReporter: ProgressReporter = nullProgressReporter,
    private val rawInput: InputStream,
) : InputStream() {

    fun copyTo(output: ResourceOutputStream) {
        output.use { output ->
            rawInput.copyTo(output)
        }
    }

    override fun read(): Int {
        val value = rawInput.read()
        if (value != -1) {
            progressReporter.next()
        }
        return value
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val count = rawInput.read(b, off, len)
        if (count > 0) {
            repeat(count) { progressReporter.next() }
        }
        return count
    }

    override fun close() = rawInput.close()
}
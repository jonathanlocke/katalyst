package jonathanlocke.katalyst.resources.streaming.io

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import java.io.InputStream

class ResourceInputStream(
    val progressReporter: ProgressReporter = nullProgressReporter,
    private val rawInput: InputStream,
) : InputStream() {

    val input = rawInput.buffered()

    fun copyTo(output: ResourceOutputStream) {
        val input = this
        output.use { output ->
            input.copyTo(output)
        }
    }

    override fun read(): Int {
        val value = input.read()
        if (value != -1) {
            progressReporter.next()
        }
        return value
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val count = input.read(b, off, len)
        if (count > 0) {
            repeat(count) { progressReporter.next() }
        }
        return count
    }

    override fun close() = input.close()
}

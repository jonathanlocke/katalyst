package jonathanlocke.katalyst.resources.streaming.io

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import java.io.OutputStream

class ResourceOutputStream(
    val progressReporter: ProgressReporter = nullProgressReporter,
    private val rawOutput: OutputStream,
) : OutputStream() {

    val output = rawOutput.buffered()

    override fun write(b: Int) {
        output.write(b)
        progressReporter.next()
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        output.write(b, off, len)
        repeat(len) { progressReporter.next() }
    }

    override fun close() {
        output.close()
    }
}
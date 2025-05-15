package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import java.io.OutputStream

class ResourceOutputStream(
    val progressReporter: ProgressReporter = nullProgressReporter,
    private val rawOutput: OutputStream,
) : OutputStream() {

    override fun write(b: Int) {
        rawOutput.write(b)
        progressReporter.next()
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        rawOutput.write(b, off, len)
        repeat(len) { progressReporter.next() }
    }

    override fun close() {
        rawOutput.close()
    }
}
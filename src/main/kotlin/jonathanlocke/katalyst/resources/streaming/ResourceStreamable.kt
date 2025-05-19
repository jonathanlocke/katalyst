package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.streaming.io.ResourceInputStream
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerMixin

interface ResourceStreamable : StatusHandlerMixin {

    enum class CopyMethod {
        Copy, CopyAndRename
    }

    fun openForReading(progressReporter: ProgressReporter = nullProgressReporter): ResourceInputStream

    fun openForWriting(
        mode: WriteMode = DoNotOverwrite,
        progressReporter: ProgressReporter = nullProgressReporter,
    ): ResourceOutputStream

    fun streamer(statusHandler: StatusHandler = this, progressReporter: ProgressReporter = nullProgressReporter) =
        ResourceStreamer(this, statusHandler, progressReporter)
}

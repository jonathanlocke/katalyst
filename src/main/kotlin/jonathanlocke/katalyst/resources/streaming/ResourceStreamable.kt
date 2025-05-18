package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.ProblemHandlerMixin
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.streaming.io.ResourceInputStream
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite

interface ResourceStreamable : ProblemHandlerMixin {

    enum class CopyMethod {
        Copy, CopyAndRename
    }

    fun openForReading(progressReporter: ProgressReporter = nullProgressReporter): ResourceInputStream

    fun openForWriting(
        mode: WriteMode = DoNotOverwrite,
        progressReporter: ProgressReporter = nullProgressReporter,
    ): ResourceOutputStream

    fun streamer(problemHandler: ProblemHandler = this, progressReporter: ProgressReporter = nullProgressReporter) =
        ResourceStreamer(this, problemHandler, progressReporter)
}

package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.streaming.WriteMode.DoNotOverwrite

interface ResourceStreamable {

    fun openForReading(progressReporter: ProgressReporter = nullProgressReporter): ResourceInputStream

    fun openForWriting(
        progressReporter: ProgressReporter = nullProgressReporter,
        mode: WriteMode = DoNotOverwrite,
    ): ResourceOutputStream

    fun streamer(): ResourceStreamer = ResourceStreamer(this)
}

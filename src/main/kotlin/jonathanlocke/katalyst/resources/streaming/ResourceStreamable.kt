package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter

interface ResourceStreamable {

    fun openForReading(progressReporter: ProgressReporter = nullProgressReporter): ResourceInputStream

    fun openForWriting(progressReporter: ProgressReporter = nullProgressReporter): ResourceOutputStream

    fun streamer(): ResourceStreamer = ResourceStreamer(this)
}

package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite

interface ResourceCopyable {

    fun copyTo(
        to: Resource,
        method: CopyMethod = CopyAndRename,
        mode: WriteMode = DoNotOverwrite,
        progressReporter: ProgressReporter = nullProgressReporter,
    )
}

enum class CopyMethod {
    Copy,
    CopyAndRename
}

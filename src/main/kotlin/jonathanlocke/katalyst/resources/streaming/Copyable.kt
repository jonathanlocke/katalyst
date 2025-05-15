package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.WriteMode.DoNotOverwrite

interface Copyable {

    fun copyTo(
        target: Resource,
        method: CopyMethod = CopyAndRename,
        mode: WriteMode = DoNotOverwrite,
        progressReporter: ProgressReporter,
    )
}
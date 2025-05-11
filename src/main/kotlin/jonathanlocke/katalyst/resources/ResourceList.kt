package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.temporal.TimeExtensions
import jonathanlocke.katalyst.data.values.temporal.TimeExtensions.Companion.profile
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.throwOnError
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.progressReporter
import jonathanlocke.katalyst.resources.streaming.WriteMode
import jonathanlocke.katalyst.resources.streaming.WriteMode.DoNotOverwrite

class ResourceList(resources: List<Resource>) : List<Resource> by resources {

    /**
     * Copies all nested resources matching the given matcher from this folder to the destination folder.
     */
    fun copyTo(
        problemHandler: ProblemHandler = throwOnError,
        target: ResourceFolder,
        mode: WriteMode = DoNotOverwrite,
        reporter: ProgressReporter = progressReporter(problemHandler, count())
    ): Boolean = profile(problemHandler.prefixed("Copying ${count()} files to $target")) {
        problemHandler.requireOrError(!target.exists(), "Target folder does not exist") {
            for (resource in this) {
                val relativePath = target.relativeTo()resource.relativeTo().path()
                resource.copyTo(target.child(relativePath), mode, reporter)
            }
        }
    }
}
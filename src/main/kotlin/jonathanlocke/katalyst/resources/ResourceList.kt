package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.temporal.TimeExtensions.Companion.profile
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.progressReporter
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite

class ResourceList(
    private val parent: ResourceFolder,
    private val problemHandler: ProblemHandler,
    private val resources: List<Resource>,
) : List<Resource> by resources {

    /**
     * Copies all nested resources matching the given matcher from this folder to the destination folder.
     */
    fun copyTo(
        to: ResourceFolder,
        method: CopyMethod = CopyAndRename,
        mode: WriteMode = DoNotOverwrite,
        progressReporter: ProgressReporter = progressReporter(
            problemHandler, resources.count(), resources.count() / 10
        ),
    ): Boolean {

        // Prefix this problem handler to give problems a context,
        val prefixedProblemHandler = problemHandler.prefixedWith("Copying ${count()} files to $to")

        // profile the copy operation,
        return profile(prefixedProblemHandler) {

            // and if the 'to' folder exists,
            prefixedProblemHandler.requireOrError(!to.exists(), "Target folder does not exist") {

                // then for each resource,
                for (resource in this) {

                    // get the path to the target we want to write to inside the 'to' folder,
                    val target = to.resource(resource.relativeTo(parent).location)

                    // and copy the resource.
                    resource.streamer(prefixedProblemHandler, progressReporter).copyTo(target, method, mode)
                }
            }
        }
    }
}
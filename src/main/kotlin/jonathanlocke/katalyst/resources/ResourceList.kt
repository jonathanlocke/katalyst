package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.temporal.TimeExtensions.Companion.profile
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.progressReporter
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.handlers.PrefixingStatusHandler.Companion.prefixedWith

class ResourceList(
    private val parent: ResourceFolder,
    private val statusHandler: StatusHandler,
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
            statusHandler, resources.count(), resources.count() / 10
        ),
    ): Boolean {

        // Prefix this status handler to give problems a context,
        val prefixedStatusHandler = statusHandler.prefixedWith("Copying ${count()} files to $to")

        // profile the copy operation,
        return profile(prefixedStatusHandler) {

            // and if the 'to' folder exists,
            prefixedStatusHandler.requireOrError(!to.exists(), "Target folder does not exist") {

                // then for each resource,
                for (resource in this) {

                    // get the path to the target we want to write to inside the 'to' folder,
                    val target = to.resource(resource.relativeTo(parent).location)

                    // and copy the resource.
                    resource.streamer(prefixedStatusHandler, progressReporter).copyTo(target, method, mode)
                }
            }
        }
    }
}
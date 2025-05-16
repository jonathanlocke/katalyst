package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.temporal.TimeExtensions.Companion.profile
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.progressReporter
import jonathanlocke.katalyst.resources.streaming.CopyMethod
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.DoNotOverwrite

class ResourceList(resources: List<Resource>) : List<Resource> by resources, ProblemSourceMixin {

    fun commonAncestor(): ResourceFolder {
        requireOrFail(isNotEmpty(), "Cannot find common ancestor of empty list")
        var ancestor = first().parent()!!
        ancestor.root()
        for (resource in this) {
            while (!ancestor.isRoot && !resource.location.isUnder(ancestor.location)) {
                ancestor = ancestor.parent()!!
            }
        }
        return ancestor
    }

    /**
     * Copies all nested resources matching the given matcher from this folder to the destination folder.
     */
    fun copyTo(
        to: ResourceFolder,
        method: CopyMethod = CopyAndRename,
        mode: WriteMode = DoNotOverwrite,
        reporter: ProgressReporter = progressReporter(this, count()),
    ): Boolean {

        // Prefix this problem handler to give problems a context,
        val problemHandler = prefixed("Copying ${count()} files to $to")

        // profile the copy operation,
        profile(problemHandler) {

            // and if the 'to' folder exists,
            problemHandler.requireOrError(!to.exists(), "Target folder does not exist") {

                // go through each resource we want to copy,
                for (resource in this) {

                    // and copy it to the path relative to the 'to' folder.
                    resource.copyTo(
                        to.resource(resource.relativeTo(commonAncestor()).location),
                        method, mode, reporter
                    )
                }
            }
        }
    }
}
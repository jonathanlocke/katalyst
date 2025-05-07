package jonathanlocke.katalyst.resources.storage

import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.temporal.TimeExtensions.Companion.profile
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ThrowOnError
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.progressReporter
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.streams.WriteMode
import jonathanlocke.katalyst.resources.streams.WriteMode.DoNotOverwrite

class ResourceStoreNodeList(nodes: List<ResourceStoreNode>) : List<ResourceStoreNode> by nodes {

    fun files() = ResourceStoreNodeList(filter { it is Resource })
    fun folders() = ResourceStoreNodeList(filter { it is ResourceFolder })
    fun count() = count(size)

    /**
     * Copies all nested resources matching the given matcher from this folder to the destination folder.
     */
    fun copyTo(
        problemHandler: ProblemHandler = ThrowOnError(),
        target: ResourceFolder,
        mode: WriteMode = DoNotOverwrite,
        reporter: ProgressReporter = progressReporter(problemHandler, count())
    ): Boolean {
        return profile(problemHandler.prefixed("Copying ${count()} files to $target")) {
            if (problemHandler.ensure(target.exists(), "Target folder does not exist")) {
                for (node in this) {
                    val relativePath = node.relativeTo(this).path()
                    node.copyTo(target.child(relativePath), mode, reporter)
                }
                true
            }
            false
        }
    }
}

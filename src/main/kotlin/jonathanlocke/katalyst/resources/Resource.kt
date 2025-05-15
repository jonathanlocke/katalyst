package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.ProblemList
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename.Companion.parseFilename
import jonathanlocke.katalyst.resources.streaming.*
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.WriteMode.Overwrite

class Resource(location: ResourceLocation) : ResourceNode(location), ResourceCopyable, ResourceStreamable {

    override fun openForReading(progressReporter: ProgressReporter) =
        resourceService.openForReading(progressReporter)

    override fun openForWriting(progressReporter: ProgressReporter, mode: WriteMode): ResourceOutputStream {
        requireOrFail(mode == Overwrite || !exists(), "Cannot overwrite existing resource: $this")

        return resourceService.openForWriting(progressReporter)
    }

    fun relativeTo(folder: ResourceFolder) = Resource(location.relativeTo(folder.location))

    override fun copyTo(
        target: Resource,
        method: CopyMethod,
        mode: WriteMode,
        progressReporter: ProgressReporter,
    ) {
        requireOrFail(target.isWritable(), "Target is not writable: $target")
        requireOrFail(target.can(mode), "Cannot overwrite target: $target")

        when (method) {
            CopyAndRename -> {
                val temporary = storeService.temporaryResource(parseFilename("copy"))
                openForReading().use { input ->
                    temporary.openForWriting(Overwrite).use { output ->
                        input.copyTo(output)
                    }
                }
                temporary.renameTo(target)
            }

            else -> copyResource(target, mode)
        }
    }

    private fun copyResource(target: Resource, mode: WriteMode) =
        openForReading().copyTo(target.openForWriting(mode))


    override fun handlers(): MutableList<ProblemHandler> {
        TODO("Not yet implemented")
    }

    override fun problems(): ProblemList {
        TODO("Not yet implemented")
    }

}

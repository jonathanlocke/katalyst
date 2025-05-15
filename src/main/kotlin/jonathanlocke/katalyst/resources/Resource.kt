package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename.Companion.parseFilename
import jonathanlocke.katalyst.resources.streaming.*
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.WriteMode.Overwrite

class Resource(location: ResourceLocation) : ResourceNode(location), Copyable, ResourceStreamable {

    override fun openForReading(): ResourceStream = store.resource(location).openForReading()
    override fun openForWriting(mode: WriteMode): ResourceStream = store.resource(location).openForWriting(mode)

    fun relativeTo(folder: ResourceFolder) = Resource(location.relativeTo(folder.location))
    override fun copyTo(
        target: Resource,
        method: CopyMethod,
        mode: WriteMode,
        progressReporter: ProgressReporter,
    ) {
        requireOrFail(target.isWritable(), "Target is not writable: $target")
        requireOrFail(target.canOverwrite(mode), "Cannot overwrite target: $target")

        when (method) {
            CopyAndRename -> {
                val temporary = store.temporaryResource(parseFilename("copy"))
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

    private fun copyResource(target: Resource, mode: WriteMode) = openForReading().copyTo(target.openForWriting(mode))

}

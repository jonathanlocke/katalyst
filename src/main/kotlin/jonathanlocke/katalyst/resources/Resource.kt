package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename.Companion.parseFilename
import jonathanlocke.katalyst.resources.streaming.CopyMethod
import jonathanlocke.katalyst.resources.streaming.CopyMethod.Copy
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.ResourceCopyable
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.Overwrite

class Resource(location: ResourceLocation) :
    ResourceNode(location),
    ResourceCopyable,
    ResourceStreamable {

    override fun openForReading(progressReporter: ProgressReporter) =
        resourceService.openForReading(progressReporter)

    override fun openForWriting(mode: WriteMode, progressReporter: ProgressReporter): ResourceOutputStream {
        requireOrFail(mode == Overwrite || !exists(), "Cannot overwrite existing resource: $this")
        return resourceService.openForWriting(progressReporter = progressReporter)
    }

    fun relativeTo(folder: ResourceFolder) = Resource(location.relativeTo(folder.location))

    override fun copyTo(
        to: Resource,
        method: CopyMethod,
        mode: WriteMode,
        progressReporter: ProgressReporter,
    ) {
        requireOrFail(to.isWritable(), "Target is not writable: $to")

        when (method) {

            CopyAndRename -> {
                val temporary = store.temporaryResource(parseFilename("copy"))
                openForReading().use { input ->
                    temporary.openForWriting(Overwrite, progressReporter).use { output ->
                        input.copyTo(output)
                    }
                }
                temporary.moveTo(to)
            }

            Copy -> copyResource(to, mode, progressReporter)
        }
    }

    private fun copyResource(to: Resource, mode: WriteMode, progressReporter: ProgressReporter) {
        openForReading().use { input ->
            to.openForWriting(mode, progressReporter).use { output ->
                input.copyTo(output)
            }
        }
    }
}

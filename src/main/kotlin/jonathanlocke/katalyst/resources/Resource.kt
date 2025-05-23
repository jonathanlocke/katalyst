package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import jonathanlocke.katalyst.resources.streaming.io.WriteMode.Overwrite
import jonathanlocke.katalyst.status.StatusHandler

class Resource(
    private val statusHandler: StatusHandler,
    location: ResourceLocation,
) : ResourceNode(statusHandler, location), ResourceStreamable {

    fun can(capability: ResourceCapability) = resourceService.capabilities.contains(capability)

    override fun openForReading(progressReporter: ProgressReporter) = resourceService.openForReading(progressReporter)

    override fun openForWriting(mode: WriteMode, progressReporter: ProgressReporter): ResourceOutputStream {
        requireOrFail(mode == Overwrite || !exists(), "Cannot overwrite existing resource: $this")
        return resourceService.openForWriting(progressReporter = progressReporter)
    }

    fun isReadable() = resourceService.isReadable()
    fun isWritable() = resourceService.isWritable()

    fun relativeTo(folder: ResourceFolder) = Resource(statusHandler, location.relativeTo(folder.location))
}

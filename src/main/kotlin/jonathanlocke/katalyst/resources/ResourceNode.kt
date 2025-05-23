package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlerMixin
import java.time.Instant

abstract class ResourceNode(
    private val statusHandler: StatusHandler,
    val location: ResourceLocation,
) : StatusHandlerMixin {

    val store = ResourceStore(statusHandler, location)

    internal val storeService = store.storeService
    internal val nodeService by lazy { store.nodeService(location) }
    internal val resourceService by lazy { store.resourceService(location) }
    internal val folderService by lazy { store.folderService(location) }

    fun size(): Bytes? = metadata()?.size
    fun createdAtUtc(): Instant? = metadata()?.createdAtUtc
    fun lastModifiedAtUtc(): Instant? = metadata()?.lastModifiedAtUtc
    fun lastAccessedAtUtc(): Instant? = metadata()?.lastAccessedAtUtc

    fun metadata(): ResourceMetadata? = nodeService.metadata()
    fun resource(location: ResourceLocation) = Resource(statusHandler, location)
    fun folder(location: ResourceLocation) = ResourceFolder(statusHandler, location)

    fun root() = ResourceFolder(statusHandler, storeService.root)
    fun parent() = location.parent?.let { resource(it) }
    fun exists() = nodeService.exists()

    fun moveTo(target: Resource) = nodeService.moveTo(target.location)
    fun delete() = nodeService.delete()
}

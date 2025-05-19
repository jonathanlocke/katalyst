package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.metadata.ResourceStoreMetadata
import jonathanlocke.katalyst.resources.services.resolvers.ResourceStoreServiceRegistry.Companion.resourceStoreServiceRegistry
import jonathanlocke.katalyst.status.StatusHandler

class ResourceStore(
    private val statusHandler: StatusHandler,
    private val root: ResourceLocation,
) {
    fun metadata(): ResourceStoreMetadata? = storeService().metadata()
    fun size(): Bytes? = storeService().metadata()?.size
    fun free(): Bytes? = storeService().metadata()?.free
    fun usable(): Bytes? = storeService().metadata()?.usable
    fun percentFree(): Percent? = free()?.percentOf(size()!!)
    fun percentUsable(): Percent? = usable()?.percentOf(size()!!)

    fun temporaryResourceLocation(baseName: Filename) =
        Resource(statusHandler, storeService().temporaryResourceLocation(baseName))

    fun temporaryFolderLocation(baseName: Filename) =
        ResourceFolder(statusHandler, storeService().temporaryFolderLocation(baseName))

    internal fun storeService() = resourceStoreServiceRegistry.resolve(root)
    internal fun nodeService(location: ResourceLocation) = storeService().node(location)
    internal fun resourceService(location: ResourceLocation) = storeService().resource(location)
    internal fun folderService(location: ResourceLocation) = storeService().folder(location)
}

package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.resolvers.ResourceStoreServiceRegistry.Companion.resourceStoreServiceRegistry

class ResourceStore(
    private val problemHandler: ProblemHandler,
    root: ResourceLocation,
) {

    val storeService = resourceStoreServiceRegistry.resolve(root)

    private fun resource(location: ResourceLocation) = Resource(problemHandler, location)
    private fun folder(location: ResourceLocation) = ResourceFolder(problemHandler, location)

    val size get() = storeService.metadata().size
    val free get() = storeService.metadata().free
    val usable get() = storeService.metadata().usable

    val percentFree: Percent get() = free.percentOf(size)
    val percentUsable: Percent get() = usable.percentOf(size)

    fun temporaryResourceLocation(baseName: Filename) = resource(storeService.temporaryResourceLocation(baseName))
    fun temporaryFolderLocation(baseName: Filename) = folder(storeService.temporaryFolderLocation(baseName))

    fun nodeService(location: ResourceLocation) = storeService.node(location)
    fun resourceService(location: ResourceLocation) = storeService.resource(location)
    fun folderService(location: ResourceLocation) = storeService.folder(location)
}

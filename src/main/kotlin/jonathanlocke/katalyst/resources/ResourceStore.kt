package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.resolvers.ResourceStoreServiceRegistry.Companion.resourceStoreServiceRegistry

class ResourceStore(root: ResourceLocation) {

    val storeService = resourceStoreServiceRegistry.resolve(root)

    fun temporaryResource(baseName: Filename) = Resource(storeService.temporaryResourceLocation(baseName))
    fun temporaryFolder(baseName: Filename) = ResourceFolder(storeService.temporaryFolderLocation(baseName))

    fun nodeService(location: ResourceLocation) = storeService.node(location)
    fun resourceService(location: ResourceLocation) = storeService.resource(location)
    fun folderService(location: ResourceLocation) = storeService.folder(location)
}

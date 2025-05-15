package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.resolvers.ResourceStoreServiceRegistry.Companion.resourceStoreServiceRegistry

class ResourceStore(location: ResourceLocation) {

    private val service = resourceStoreServiceRegistry.resolve(location)

    fun temporaryResource(baseName: Filename) = Resource(service.temporaryResourceLocation(baseName))
    fun resource(location: ResourceLocation) = service.resource(location)
    fun folder(location: ResourceLocation) = service.folder(location)
}

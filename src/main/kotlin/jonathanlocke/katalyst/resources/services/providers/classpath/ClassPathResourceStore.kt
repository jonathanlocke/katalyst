package jonathanlocke.katalyst.resources.services.providers.classpath

import jonathanlocke.katalyst.resources.capabilities.ResourceStoreCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceProximity.Local
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.status.StatusHandlerMixin

class ClassPathResourceStore(
    override val root: ResourceLocation,
) : ResourceStoreService, StatusHandlerMixin {

    override val capabilities = setOf(Resolve)
    override val proximity = Local

    override fun metadata() = null
    override fun resource(location: ResourceLocation) = ClassPathResource(this, location)
    override fun folder(location: ResourceLocation) = ClassPathFolder(this, location)
    override fun node(location: ResourceLocation) = throw unimplemented()
    override fun temporaryResourceLocation(baseName: Filename) = throw unimplemented()
    override fun temporaryFolderLocation(baseName: Filename) = throw unimplemented()
}

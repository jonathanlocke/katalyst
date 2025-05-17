package jonathanlocke.katalyst.resources.services.providers.classpath

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceProximity.Local
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService

class ClassPathResourceStore(
    override val root: ResourceLocation,
) : ResourceStoreService, ProblemSourceMixin {

    override val proximity = Local
    override val size = bytes(0)
    override val usable = bytes(0)
    override val free = bytes(0)

    override fun resource(location: ResourceLocation): ResourceService = ClassPathResource(this, location)

    override fun folder(location: ResourceLocation): ResourceFolderService {
        TODO("Not yet implemented")
    }

    override fun node(location: ResourceLocation): ResourceNodeService {
        TODO("Not yet implemented")
    }

    override fun temporaryResourceLocation(baseName: Filename): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun temporaryFolderLocation(baseName: Filename): ResourceLocation {
        TODO("Not yet implemented")
    }

}
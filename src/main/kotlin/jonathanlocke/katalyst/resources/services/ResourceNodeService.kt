package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.problems.ProblemSource
import jonathanlocke.katalyst.resources.ResourceCapability
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata

interface ResourceNodeService : ResourceMetadata, ProblemSource {

    val store: ResourceStoreService
    val location: ResourceLocation
    val isFolder: Boolean
    val isResource get() = !isFolder

    fun can(capability: ResourceCapability): Boolean
}

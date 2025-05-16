package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.problems.ProblemSource
import jonathanlocke.katalyst.resources.ResourceCapability
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata

interface ResourceNodeService : ProblemSource {

    val store: ResourceStoreService
    val location: ResourceLocation
    val metadata: ResourceMetadata

    val isFolder: Boolean
    val isResource get() = !isFolder

    fun can(capability: ResourceCapability): Boolean
    fun moveTo(target: ResourceLocation): Boolean
    fun delete(): Boolean
}

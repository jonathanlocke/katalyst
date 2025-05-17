package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata

interface ResourceNodeService : ProblemSourceMixin {

    val store: ResourceStoreService
    val location: ResourceLocation
    val metadata: ResourceMetadata

    val isFolder: Boolean
    val isResource get() = !isFolder

    fun moveTo(target: ResourceLocation): Boolean
    fun delete(): Boolean
    fun exists(): Boolean
}

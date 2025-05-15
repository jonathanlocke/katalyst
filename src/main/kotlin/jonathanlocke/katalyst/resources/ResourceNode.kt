package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.problems.ProblemSource
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Write
import jonathanlocke.katalyst.resources.location.ResourceLocation

abstract class ResourceNode(val location: ResourceLocation) : ProblemSource {

    protected val storeService = ResourceStore(location)
    protected val nodeService = storeService.nodeService(location)
    protected val resourceService = storeService.resourceService(location)
    protected val folderService = storeService.folderService(location)

    val metadata = nodeService.metadata
    val size = metadata.size
    val createdAtUtc = metadata.createdAtUtc
    val lastModifiedAtUtc = metadata.lastModifiedAtUtc
    val lastAccessedAtUtc = metadata.lastAccessedAtUtc

    fun exists() = nodeService.can(Resolve)
    fun isReadable() = nodeService.can(Read)
    fun isWritable() = nodeService.can(Write)
}
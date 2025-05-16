package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Write
import jonathanlocke.katalyst.resources.location.ResourceLocation

abstract class ResourceNode(val location: ResourceLocation) : ProblemSourceMixin {

    protected val store = ResourceStore(location)
    protected val storeService = store.storeService
    protected val nodeService by lazy { store.nodeService(location) }
    protected val resourceService by lazy { store.resourceService(location) }
    protected val folderService by lazy { store.folderService(location) }

    val metadata = nodeService.metadata
    val size = metadata.size
    val createdAtUtc = metadata.createdAtUtc
    val lastModifiedAtUtc = metadata.lastModifiedAtUtc
    val lastAccessedAtUtc = metadata.lastAccessedAtUtc

    fun root() = ResourceFolder(storeService.root)
    fun parent() = location.parent?.let { ResourceFolder(it) }
    fun exists() = nodeService.can(Resolve)
    fun isReadable() = nodeService.can(Read)
    fun isWritable() = nodeService.can(Write)

    fun moveTo(target: Resource) = nodeService.moveTo(target.location)
    fun delete() = nodeService.delete()
}
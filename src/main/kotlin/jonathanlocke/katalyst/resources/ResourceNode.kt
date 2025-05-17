package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation

abstract class ResourceNode(
    private val problemHandler: ProblemHandler,
    val location: ResourceLocation,
) : ProblemSourceMixin {

    val store = ResourceStore(problemHandler, location)

    protected val storeService = store.storeService
    protected val nodeService by lazy { store.nodeService(location) }
    protected val resourceService by lazy { store.resourceService(location) }
    protected val folderService by lazy { store.folderService(location) }

    fun resource(location: ResourceLocation) = Resource(problemHandler, location)
    fun folder(location: ResourceLocation) = ResourceFolder(problemHandler, location)

    val metadata = nodeService.metadata
    val size = metadata.size
    val createdAtUtc = metadata.createdAtUtc
    val lastModifiedAtUtc = metadata.lastModifiedAtUtc
    val lastAccessedAtUtc = metadata.lastAccessedAtUtc

    fun root() = ResourceFolder(problemHandler, storeService.root)
    fun parent() = location.parent?.let { resource(it) }
    fun exists() = nodeService.exists()

    fun moveTo(target: Resource) = nodeService.moveTo(target.location)
    fun delete() = nodeService.delete()
}
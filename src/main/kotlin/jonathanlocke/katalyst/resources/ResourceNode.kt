package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import java.time.Instant

abstract class ResourceNode(
    private val problemHandler: ProblemHandler,
    val location: ResourceLocation,
) : ProblemSourceMixin {

    val store = ResourceStore(problemHandler, location)

    fun size(): Bytes? = metadata()?.size
    fun createdAtUtc(): Instant? = metadata()?.createdAtUtc
    fun lastModifiedAtUtc(): Instant? = metadata()?.lastModifiedAtUtc
    fun lastAccessedAtUtc(): Instant? = metadata()?.lastAccessedAtUtc

    fun metadata(): ResourceMetadata? = nodeService.metadata()
    fun resource(location: ResourceLocation) = Resource(problemHandler, location)
    fun folder(location: ResourceLocation) = ResourceFolder(problemHandler, location)

    fun root() = ResourceFolder(problemHandler, storeService.root)
    fun parent() = location.parent?.let { resource(it) }
    fun exists() = nodeService.exists()

    fun moveTo(target: Resource) = nodeService.moveTo(target.location)
    fun delete() = nodeService.delete()

    internal val storeService = store.storeService()
    internal val nodeService by lazy { store.nodeService(location) }
    internal val resourceService by lazy { store.resourceService(location) }
    internal val folderService by lazy { store.folderService(location) }
}
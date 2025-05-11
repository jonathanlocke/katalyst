package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.services.resolvers.ResourceStoreServiceRegistry.Companion.resourceStoreServiceRegistry

abstract class ResourceNode(val location: ResourceLocation) : ResourceMetadata {

    val store = resourceStoreServiceRegistry.resolve(location)

    fun exists() = store.node(location).exists()
    fun isReadable() = store.node(location).isReadable()
    fun isWritable() = store.node(location).isWritable()

    override val size = store.node(location).size
    override val createdAtUtc = store.node(location).createdAtUtc
    override val lastModifiedAtUtc = store.node(location).lastModifiedAtUtc
    override val lastAccessedAtUtc = store.node(location).lastAccessedAtUtc
}
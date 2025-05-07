package jonathanlocke.katalyst.resources.storage

import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.storage.location.ResourceLocation

interface ResourceStoreNode : ResourceMetadata, Renamable {

    val store: ResourceStore
    val location: ResourceLocation
}

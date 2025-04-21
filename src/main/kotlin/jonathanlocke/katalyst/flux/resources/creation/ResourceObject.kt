package jonathanlocke.katalyst.flux.resources.creation

import jonathanlocke.katalyst.flux.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.flux.resources.storage.ResourceStorageSystem


interface ResourceObject : ResourceMetadata {

    val parent: ResourceFolder
    val storageSystem: ResourceStorageSystem
}

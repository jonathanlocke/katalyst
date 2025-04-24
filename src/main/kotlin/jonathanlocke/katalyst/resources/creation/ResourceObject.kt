package jonathanlocke.katalyst.resources.creation

import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.storage.ResourceStorageSystem


interface ResourceObject : ResourceMetadata {

    val parent: ResourceFolder
    val storageSystem: ResourceStorageSystem
}

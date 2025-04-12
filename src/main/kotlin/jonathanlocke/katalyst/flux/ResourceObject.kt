package jonathanlocke.katalyst.flux

import jonathanlocke.katalyst.flux.metadata.ResourceMetadata

interface ResourceObject : ResourceMetadata {

    val parent: ResourceFolder
    val storageSystem: ResourceStorageSystem
}

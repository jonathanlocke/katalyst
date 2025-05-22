package jonathanlocke.katalyst.resources.capabilities

class ResourceStoreCapability(val name: String) {

    companion object {
        val Resolve = ResourceStoreCapability("Resolve")
        val GetMetadata = ResourceStoreCapability("GetMetadata")
        val GetTemporaryResourceLocation = ResourceStoreCapability("GetTemporaryResourceLocation")
        val GetTemporaryFolderLocation = ResourceStoreCapability("GetTemporaryFolderLocation")
    }
}

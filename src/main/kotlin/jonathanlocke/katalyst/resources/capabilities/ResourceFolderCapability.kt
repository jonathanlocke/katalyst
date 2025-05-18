package jonathanlocke.katalyst.resources.capabilities

class ResourceFolderCapability(val name: String) {

    companion object {
        val Resolve = ResourceFolderCapability("Resolve")
        val GetMetadata = ResourceFolderCapability("GetMetadata")
        val Move = ResourceFolderCapability("Move")
        val Delete = ResourceFolderCapability("Delete")
        val ListFiles = ResourceFolderCapability("ListFiles")
        val ListFolders = ResourceFolderCapability("ListFolders")
    }
}
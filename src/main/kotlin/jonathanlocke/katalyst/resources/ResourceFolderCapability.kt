package jonathanlocke.katalyst.resources

class ResourceFolderCapability(val name: String) {

    companion object {
        val Move = ResourceFolderCapability("Move")
        val Delete = ResourceFolderCapability("Delete")
    }
}
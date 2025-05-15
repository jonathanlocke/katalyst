package jonathanlocke.katalyst.resources

class ResourceCapability(val name: String) {

    companion object {
        val Resolve = ResourceCapability("Resolve")
        val Move = ResourceCapability("Move")
        val Delete = ResourceCapability("Delete")
        val Write = ResourceCapability("WriteTo")
        val Read = ResourceCapability("ReadFrom")
    }
}
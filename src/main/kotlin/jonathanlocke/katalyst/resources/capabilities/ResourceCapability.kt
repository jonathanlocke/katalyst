package jonathanlocke.katalyst.resources.capabilities

class ResourceCapability(val name: String) {

    companion object {
        val Resolve = ResourceCapability("Resolve")
        val Move = ResourceCapability("Move")
        val Delete = ResourceCapability("Delete")
        val Read = ResourceCapability("Read")
        val Write = ResourceCapability("Write")
    }
}
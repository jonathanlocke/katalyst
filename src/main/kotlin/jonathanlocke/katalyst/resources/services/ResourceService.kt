package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.capabilities.ResourceCapability
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable

interface ResourceService : ResourceNodeService, ResourceStreamable {

    fun isReadable(): Boolean
    fun isWritable(): Boolean

    override val isFolder: Boolean get() = false

    val capabilities: Set<ResourceCapability>
}
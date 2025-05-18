package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.capabilities.ResourceCapability
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable

interface ResourceService : ResourceNodeService, ResourceStreamable {

    val capabilities: Set<ResourceCapability>

    fun isReadable(): Boolean
    fun isWritable(): Boolean
}
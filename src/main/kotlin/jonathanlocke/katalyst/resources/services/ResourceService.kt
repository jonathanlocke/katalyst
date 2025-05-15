package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.streaming.ResourceStreamable

interface ResourceService : ResourceNodeService, ResourceStreamable {

    override val isFolder: Boolean get() = false

    fun moveTo(target: ResourceLocation): Boolean
}
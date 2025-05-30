package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.status.StatusHandlerMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata

interface ResourceNodeService : StatusHandlerMixin {

    val store: ResourceStoreService
    val location: ResourceLocation
    val isFolder get() = this is ResourceFolderService
    val isResource get() = !isFolder

    fun metadata(): ResourceMetadata?
    fun moveTo(target: ResourceLocation): Boolean
    fun delete(): Boolean
    fun exists(): Boolean
}

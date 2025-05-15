package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.ResourceFolder.Companion.TopLevel
import jonathanlocke.katalyst.resources.ResourceFolder.Recursion
import jonathanlocke.katalyst.resources.location.ResourceLocation

interface ResourceFolderService : ResourceNodeService {

    override val isFolder: Boolean get() = true

    fun clear(): Boolean
    fun mkdirs(): Boolean
    fun delete(): Boolean
    fun moveTo(target: ResourceLocation): Boolean
    fun resources(recursion: Recursion = TopLevel): List<ResourceLocation>
    fun folders(recursion: Recursion = TopLevel): List<ResourceLocation>
}

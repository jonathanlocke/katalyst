package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.ResourceFolder.Companion.TopLevel
import jonathanlocke.katalyst.resources.ResourceFolder.Recursion
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability
import jonathanlocke.katalyst.resources.location.ResourceLocation

interface ResourceFolderService : ResourceNodeService {

    val capabilities: Set<ResourceFolderCapability>

    fun clear(): Boolean
    fun mkdirs(): Boolean

    fun resources(recursion: Recursion = TopLevel): List<ResourceLocation>
    fun folders(recursion: Recursion = TopLevel): List<ResourceLocation>
}

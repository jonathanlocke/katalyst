package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.ResourceFolder.FolderAccess
import jonathanlocke.katalyst.resources.ResourceFolder.FolderAccess.TopLevelOnly
import jonathanlocke.katalyst.resources.location.path.Filename

interface ResourceFolderService : ResourceNodeService {

    override fun isFolder() = true

    fun clear()
    fun mkdirs()
    fun delete(): Boolean
    fun renameTo(target: ResourceFolderService): Boolean

    fun resource(filename: Filename): ResourceService
    fun resources(traversal: FolderAccess = TopLevelOnly): List<ResourceService>

    fun folder(filename: Filename): ResourceFolderService
    fun folders(traversal: FolderAccess = TopLevelOnly): List<ResourceFolderService>

    fun isEmpty() = resources().size == 0 && folders().size == 0
}

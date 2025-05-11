package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.ResourceFolder.FolderAccessMode
import jonathanlocke.katalyst.resources.ResourceFolder.FolderAccessMode.TopLevel
import jonathanlocke.katalyst.resources.location.path.Filename

interface ResourceFolderService : ResourceNodeService {

    override fun isFolder() = true

    fun clear()
    fun mkdirs()
    fun delete(): Boolean
    fun renameTo(target: ResourceFolderService): Boolean

    fun resource(filename: Filename): ResourceService
    fun resources(access: FolderAccessMode = TopLevel): List<ResourceService>

    fun folder(filename: Filename): ResourceFolderService
    fun folders(access: FolderAccessMode = TopLevel): List<ResourceFolderService>

    fun isEmpty() = resources().size == 0 && folders().size == 0
}

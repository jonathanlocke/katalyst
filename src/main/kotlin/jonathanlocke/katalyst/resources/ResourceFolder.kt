package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename

class ResourceFolder(location: ResourceLocation) : ResourceNode(location) {

    enum class FolderAccess {
        Nested,
        TopLevelOnly
    }

    fun folder(filename: Filename) = service().folder(filename)
    fun clear() = service().clear()
    fun delete() = service().delete()
    fun mkdirs() = service().mkdirs()
    fun resource(filename: Filename) = service().resource(filename)

    fun resources(mode: FolderAccess) =
        ResourceList(service().resources(mode).map { Resource(it.location) })

    fun folders(mode: FolderAccess) =
        ResourceFolderList(service().folders(mode).map { ResourceFolder(it.location) })

    private fun service() = store.folder(location)
}
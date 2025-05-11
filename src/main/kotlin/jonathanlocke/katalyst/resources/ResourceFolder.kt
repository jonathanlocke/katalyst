package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.ResourceFolder.FolderAccessMode.TopLevel
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import kotlin.Int.Companion.MAX_VALUE

class ResourceFolder(location: ResourceLocation) : ResourceNode(location) {

    enum class FolderAccessMode(val levels: Int) {
        TopLevel(1),
        Nested(MAX_VALUE)
    }

    fun folder(filename: Filename) = service().folder(filename)
    fun clear() = service().clear()
    fun delete() = service().delete()
    fun mkdirs() = service().mkdirs()
    fun resource(filename: Filename) = service().resource(filename)

    fun resources(mode: FolderAccessMode = TopLevel) =
        ResourceList(service().resources(mode).map { Resource(it.location) })

    fun folders(mode: FolderAccessMode = TopLevel) =
        ResourceFolderList(service().folders(mode).map { ResourceFolder(it.location) })

    private fun service() = store.folder(location)
}
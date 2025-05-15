package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import kotlin.Int.Companion.MAX_VALUE

class ResourceFolder(location: ResourceLocation) : ResourceNode(location) {

    class Recursion(val levels: Int)

    companion object {
        val TopLevel = Recursion(1)
        val Nested = Recursion(MAX_VALUE)
    }

    fun folder(filename: Filename) = service().folder(filename)
    fun clear() = service().clear()
    fun delete() = service().delete()
    fun mkdirs() = service().mkdirs()
    fun resource(relativeLocation: ResourceLocation) = Resource(relativeLocation.relativeTo(location))

    fun resources(mode: Recursion = TopLevel) =
        ResourceList(service().resources(mode).map { Resource(it.location) })

    fun folders(mode: Recursion = TopLevel) =
        ResourceFolderList(service().folders(mode).map { ResourceFolder(it.location) })

    private fun service() = store.folder(location)
}
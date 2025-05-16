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

    val isRoot = location.isRoot

    fun folder(filename: Filename) = ResourceFolder(location.child(filename))
    fun resource(relativeLocation: ResourceLocation) = Resource(relativeLocation.relativeTo(location))

    fun clear() = folderService.clear()
    fun mkdirs() = folderService.mkdirs()

    fun resources(mode: Recursion = TopLevel) = ResourceList(this, folderService.resources(mode).map { Resource(it) })
}
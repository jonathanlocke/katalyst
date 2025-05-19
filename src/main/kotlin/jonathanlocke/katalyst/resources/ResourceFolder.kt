package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.status.StatusHandler
import kotlin.Int.Companion.MAX_VALUE

class ResourceFolder(
    private val statusHandler: StatusHandler,
    location: ResourceLocation,
) : ResourceNode(statusHandler, location) {

    class Recursion(val levels: Int)

    companion object {
        val TopLevel = Recursion(1)
        val Nested = Recursion(MAX_VALUE)
    }

    val isRoot = location.isRoot

    fun folder(filename: Filename) = folder(location.resolve(filename))
    fun relativeResource(relativeLocation: ResourceLocation) =
        resource(relativeLocation.relativeTo(location))

    fun clear() = folderService.clear()
    fun mkdirs() = folderService.mkdirs()

    fun resources(mode: Recursion = TopLevel) = ResourceList(
        this, statusHandler, folderService.resources(mode).map { Resource(statusHandler, it) })
}
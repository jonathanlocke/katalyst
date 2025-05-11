package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.problems.ProblemSource
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import java.nio.file.Path

interface ResourceNodeService : ResourceMetadata, ProblemSource {

    val store: ResourceStoreService
    val location: ResourceLocation
    val parent: ResourceFolderService?
    val path: Path get() = location.path

    fun isResource() = !isFolder()
    fun isFolder(): Boolean

    fun exists(): Boolean
    fun isReadable(): Boolean
    fun isWritable(): Boolean
    fun relativeTo(folder: ResourceFolderService): Path
}

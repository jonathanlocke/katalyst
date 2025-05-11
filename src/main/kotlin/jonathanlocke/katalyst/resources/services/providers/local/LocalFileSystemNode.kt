package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import java.nio.file.Files
import java.nio.file.Path

abstract class LocalFileSystemNode(override val location: ResourceLocation) :
    ResourceNodeService,
    ProblemSourceMixin {

    override fun relativeTo(folder: ResourceFolderService): Path =
        folder.location.path.relativize(location.path)

    override fun exists() = Files.exists(path)
    override fun isReadable() = Files.isReadable(path)
    override fun isWritable() = Files.isWritable(path)

    override val parent: ResourceFolderService?
        get() = location.parent?.let { store.folder(it) }
}

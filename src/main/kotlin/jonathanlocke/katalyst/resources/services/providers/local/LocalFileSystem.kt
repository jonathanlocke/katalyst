package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.toBytes
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceProximity.Local
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.location.path.Paths.Companion.isRoot
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import java.nio.file.Files
import kotlin.io.path.name

class LocalFileSystem(
    override val root: ResourceLocation,
) : ResourceStoreService, ProblemSourceMixin {

    init {
        requireOrFail(root.scheme.equals("file"), "Not a local filesystem: $root")
        requireOrFail(root.path.isRoot(), "Not a local filesystem root: $root")
        requireOrFail(Files.exists(root.path), "Local filesystem root does not exist: $root")
        requireOrFail(Files.isDirectory(root.path), "Local filesystem root is not a directory: $root")
    }

    override val proximity = Local
    override val size = fileStore().totalSpace.toBytes()
    override val usable = fileStore().usableSpace.toBytes()
    override val free = fileStore().unallocatedSpace.toBytes()

    override fun resource(location: ResourceLocation): ResourceService = LocalFile(this, location)

    override fun folder(location: ResourceLocation): ResourceFolderService = LocalFolder(this, location)

    override fun node(location: ResourceLocation): ResourceNodeService = if (Files.isRegularFile(location.path)) {
        resource(location)
    } else {
        folder(location)
    }

    override fun temporaryResourceLocation(baseName: Filename): ResourceLocation =
        ResourceLocation(Files.createTempFile(baseName.name, ".tmp").toUri())

    override fun temporaryFolderLocation(baseName: Filename): ResourceLocation =
        ResourceLocation(Files.createTempFile(baseName.name, "").toUri())

    private fun fileStore() = Files.getFileStore(root.path)
    override fun toString(): String = root.toString()
}
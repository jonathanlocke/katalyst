package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.toBytes
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceProximity.Local
import jonathanlocke.katalyst.resources.location.ResourceScheme
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import java.nio.file.Files
import kotlin.io.path.name

class LocalFileSystem(override val location: ResourceLocation) : ResourceStoreService, ProblemSourceMixin {

    override val scheme = ResourceScheme("file")

    override fun resource(location: ResourceLocation): ResourceService =
        LocalFile(this, location)

    override fun folder(location: ResourceLocation): ResourceFolderService =
        LocalFolder(this, location)

    override fun node(location: ResourceLocation): ResourceNodeService =
        if (Files.isRegularFile(location.path)) {
            resource(location)
        } else {
            folder(location)
        }

    override val root = folder(location.root)
    override val proximity = Local
    override val size = fileStore().totalSpace.toBytes()
    override val usable = fileStore().usableSpace.toBytes()
    override val free = fileStore().unallocatedSpace.toBytes()

    override fun temporaryResource(baseName: Filename): ResourceService =
        resource(ResourceLocation(Files.createTempFile(baseName.name, ".tmp").toUri()))

    override fun temporaryFolder(baseName: Filename): ResourceFolderService =
        folder(ResourceLocation(Files.createTempFile(baseName.name, "").toUri()))

    private fun fileStore() = Files.getFileStore(location.path)
}
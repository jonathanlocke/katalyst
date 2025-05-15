package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.problems.exceptions.ExceptionTrait
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.ResourceFolder.Recursion
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import java.nio.file.Files

class LocalFolder(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : LocalFileSystemNode(location), ResourceFolderService, ExceptionTrait {

    override fun clear(): Boolean {
        var errors = 0
        Files.walk(location.path).sorted(Comparator.reverseOrder()).forEach {
            if (!tryCatch(this) { Files.delete(it) }) {
                errors++
            }
        }
        return false
    }

    override fun mkdirs() = tryCatch(this) {
        if (!can(Resolve)) {
            Files.createDirectories(location.path)
        }
    }

    override fun resources(recursion: Recursion): List<ResourceLocation> =
        Files.walk(location.path, recursion.levels)
            .filter { Files.isRegularFile(it) }
            .map { it -> ResourceLocation(it) }.toList()

    override fun folders(access: Recursion): List<ResourceLocation> =
        Files.walk(location.path, access.levels)
            .filter { Files.isDirectory(it) }
            .map { it -> ResourceLocation(it) }.toList()
}

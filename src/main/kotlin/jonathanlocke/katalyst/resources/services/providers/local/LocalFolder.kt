package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.resources.ResourceFolder.Recursion
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.Delete
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.ListFiles
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.ListFolders
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.Move
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import java.nio.file.Files

class LocalFolder(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : LocalFileStoreNode(location), ResourceFolderService {


    override val capabilities = setOf(Resolve, Move, Delete, ListFolders, ListFiles)

    override fun clear(): Boolean {

        var errors = 0

        // Walk through files in depth-first order,
        Files.walk(location.path).forEach {

            // and if we can't delete the file,
            if (!this.tryBoolean("Could not delete: $it") { Files.deleteIfExists(it) }) {

                // then increment the error counter,
                errors++
            }
        }

        // If we had any errors,
        if (errors > 0) {

            // show how many.
            error("Could not delete $errors files under: $location")
            return false
        }

        return true
    }

    override fun mkdirs() = tryBoolean("Could not create directory path to: $location") {
        if (!exists()) {
            Files.createDirectories(location.path)
        }
        true
    }

    override fun resources(recursion: Recursion): List<ResourceLocation> =
        Files.walk(location.path, recursion.levels)
            .filter { Files.isRegularFile(it) }
            .map { it -> ResourceLocation(it) }.toList()

    override fun folders(recursion: Recursion): List<ResourceLocation> =
        Files.walk(location.path, recursion.levels)
            .filter { Files.isDirectory(it) }
            .map { it -> ResourceLocation(it) }.toList()
}

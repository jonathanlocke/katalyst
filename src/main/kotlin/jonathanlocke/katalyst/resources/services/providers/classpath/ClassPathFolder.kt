package jonathanlocke.katalyst.resources.services.providers.classpath

import jonathanlocke.katalyst.resources.ResourceFolder.Recursion
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.ListFiles
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.ListFolders
import jonathanlocke.katalyst.resources.capabilities.ResourceFolderCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceStoreService

class ClassPathFolder(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : ClassPathNode(location), ResourceFolderService {

    override val capabilities = setOf(Resolve, ListFolders, ListFiles)

    override fun metadata() = throw unimplemented()
    override fun exists() = resources().isNotEmpty() || folders().isNotEmpty()
    override fun clear() = throw unimplemented()
    override fun moveTo(target: ResourceLocation) = throw unimplemented()
    override fun delete() = throw unimplemented()
    override fun mkdirs() = throw unimplemented()

    override fun resources(recursion: Recursion) = scanChildren { it }
    override fun folders(recursion: Recursion) = scanChildren {

        // If the relative path of the child is two levels deep (like 'a/b.txt'),
        if (it.relativeTo(location).path.count() == 2) {

            // then the first level is a folder ('a').
            location.parent
        } else {
            null
        }
    }
}

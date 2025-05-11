package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.toBytes
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.services.ResourceFolderService
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import kotlin.Int.Companion.MAX_VALUE

class LocalFolder(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : LocalFileSystemNode(location), ResourceFolderService {

    override val size: Bytes get() = Files.size(location.path).toBytes()
    override val createdAtUtc get() = attributes.creationTime().toInstant()
    override val lastModifiedAtUtc get() = attributes.lastModifiedTime().toInstant()
    override val lastAccessedAtUtc get() = attributes.lastAccessTime().toInstant()

    private val attributes get() = Files.readAttributes(location.path, BasicFileAttributes::class.java)

    override fun clear() = Files.walk(location.path).sorted(Comparator.reverseOrder()).forEach { Files.delete(it) }

    override fun mkdirs() {
        Files.createDirectories(location.path)
    }

    override fun delete() = Files.deleteIfExists(location.path)

    override fun renameTo(target: ResourceFolderService): Boolean {
        requireOrFail(store == target.store, "Cannot rename across filesystems")
        requireOrFail(target.isWritable(), "Target is not writable: $target")
        return Files.move(path, target.location.path) != null
    }

    override fun resource(filename: Filename): ResourceService = store.resource(location.child(filename))
    override fun folder(filename: Filename): ResourceFolderService = store.folder(location.child(filename))

    override fun resources(recurse: Boolean): List<ResourceService> =
        Files.walk(location.path, if (recurse) MAX_VALUE else 1).filter { Files.isRegularFile(it) }
            .map { resource(Filename(it.fileName)) }.toList()

    override fun folders(recurse: Boolean): List<ResourceFolderService> =
        Files.walk(location.path, if (recurse) MAX_VALUE else 1).filter { Files.isDirectory(it) }
            .map { folder(Filename(it.fileName)) }.toList()
}
package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.path.Filename.Companion.parseFilename
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.streaming.CopyMethod
import jonathanlocke.katalyst.resources.streaming.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streaming.WriteMode
import jonathanlocke.katalyst.resources.streaming.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.resources.streaming.WriteMode.Overwrite
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant

class LocalFile(
    override val store: ResourceStoreService,
    override val location: ResourceLocation
) : LocalFileSystemNode(location), ResourceService {

    override val size: Bytes = bytes(Files.size(path))
    override val lastModifiedAtUtc: Instant get() = attributes().lastModifiedTime().toInstant()
    override val lastAccessedAtUtc: Instant get() = attributes().lastAccessTime().toInstant()
    override val createdAtUtc: Instant get() = attributes().creationTime().toInstant()

    override fun canOverwrite(mode: WriteMode): Boolean = when (mode) {
        Overwrite -> true
        DoNotOverwrite -> !exists()
    }

    override fun openForReading(): InputStream = Files.newInputStream(path).buffered()

    override fun openForWriting(mode: WriteMode): OutputStream {
        requireOrFail(canOverwrite(mode), "Cannot overwrite: $path")
        requireOrFail(isWritable(), "File is not writable: $path")
        return Files.newOutputStream(path).buffered()
    }

    override fun renameTo(target: ResourceService): Boolean {
        requireOrFail(store == target.store, "Cannot rename across filesystems")
        requireOrFail(target.isWritable(), "Target is not writable: $target")
        return Files.move(path, target.location.path) != null
    }

    override fun copyTo(target: ResourceService, method: CopyMethod, mode: WriteMode) {
        requireOrFail(target.isWritable(), "Target is not writable: $target")
        requireOrFail(target.canOverwrite(mode), "Cannot overwrite target: $target")

        when (method) {
            CopyAndRename -> {
                val temporary = store.temporaryResource(parseFilename("copy"))
                openForReading().use { input ->
                    temporary.openForWriting(Overwrite).use { output ->
                        input.copyTo(output)
                    }
                }
                temporary.renameTo(target)
            }

            else -> openForReading().use { input ->
                target.openForWriting(mode).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun attributes(): BasicFileAttributes = Files.readAttributes(location.path, BasicFileAttributes::class.java)
}

package jonathanlocke.katalyst.resources.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.ResourceBase
import jonathanlocke.katalyst.resources.metadata.Filename
import jonathanlocke.katalyst.resources.proximity.ResourceProximity
import jonathanlocke.katalyst.resources.storage.ResourceFolder
import jonathanlocke.katalyst.resources.storage.ResourceStore
import jonathanlocke.katalyst.resources.storage.location.ResourceScheme
import jonathanlocke.katalyst.resources.streams.Copyable
import jonathanlocke.katalyst.resources.streams.WriteMode
import jonathanlocke.katalyst.resources.streams.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.resources.streams.WriteMode.Overwrite
import java.io.*
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant

class LocalFile(override val path: Path) : ResourceBase() {

    override val parent: ResourceFolder get() = TODO("Not yet implemented")
    override val store: ResourceStore = LocalFileStore()
    override val exists: Boolean = Files.exists(path)
    override val proximity: ResourceProximity = ResourceProximity.Local
    override val size: Bytes = bytes(Files.size(path))
    override val uri: URI = path.toUri()
    override val scheme = ResourceScheme(uri.scheme)
    override val filename: Filename = Filename(path)
    override val lastModifiedAtUtc: Instant get() = attributes().lastModifiedTime().toInstant()
    override val lastAccessedAtUtc: Instant get() = attributes().lastAccessTime().toInstant()
    override val createdAtUtc: Instant get() = attributes().creationTime().toInstant()
    override val isWritable: Boolean get() = Files.isWritable(path)
    override val isReadable: Boolean get() = Files.isReadable(path)

    fun asFile(): File = path.toFile()

    private fun attributes(): BasicFileAttributes = Files.readAttributes(path, BasicFileAttributes::class.java)

    override fun onOpenForReading(): InputStream = FileInputStream(asFile())

    fun canOverwrite(mode: WriteMode): Boolean = when (mode) {
        Overwrite -> true
        DoNotOverwrite -> !exists
    }

    override fun onOpenForWriting(mode: WriteMode): OutputStream {
        canOverwrite(mode) || throw Exception("Cannot overwrite: $path")
        return FileOutputStream(asFile())
    }

    override fun rename(target: Resource) {
        TODO("Not yet implemented")
    }

    override fun copyTo(target: Resource, method: Copyable.CopyMethod, mode: WriteMode) {
        TODO("Not yet implemented")
    }
}

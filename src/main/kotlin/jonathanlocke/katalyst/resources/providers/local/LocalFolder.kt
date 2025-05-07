package jonathanlocke.katalyst.resources.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.metadata.Extension
import jonathanlocke.katalyst.resources.metadata.Filename
import jonathanlocke.katalyst.resources.proximity.ResourceProximity
import jonathanlocke.katalyst.resources.storage.ResourceFolder
import jonathanlocke.katalyst.resources.storage.ResourceFolderBase
import jonathanlocke.katalyst.resources.storage.ResourceStore
import jonathanlocke.katalyst.resources.storage.ResourceStoreNodeList
import jonathanlocke.katalyst.resources.storage.location.ResourceScheme
import java.net.URI
import java.nio.file.Path
import java.time.Instant

class LocalFolder : ResourceFolderBase() {

    override fun absolute(): LocalFolder {
        TODO("Not yet implemented")
    }

    override fun delete(): Boolean {
        TODO("Not yet implemented")
    }

    override fun exists(): Boolean {
        TODO("Not yet implemented")
    }

    override val exists: Boolean
        get() = TODO("Not yet implemented")

    override fun folder(path: String?): T {
        TODO("Not yet implemented")
    }

    override fun folders(): ObjectList<T> {
        TODO("Not yet implemented")
    }

    override fun isMaterialized(): Boolean {
        TODO("Not yet implemented")
    }

    override val isWritable: Boolean
        get() = TODO("Not yet implemented")

    override fun newFolder(relativePath: ResourcePath): ResourceFolder {
        TODO("Not yet implemented")
    }

    override fun parent(): ResourceFolder {
        TODO("Not yet implemented")
    }

    override fun renameTo(folder: ResourceFolder): Boolean {
        TODO("Not yet implemented")
    }

    override val parent: ResourceFolder
        get() = TODO("Not yet implemented")

    override fun renameTo(folder: ResourceFolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun resource(path: Path): Resource {
        TODO("Not yet implemented")
    }

    override fun resourceFolderIdentifier(): ResourceFolderIdentifier {
        TODO("Not yet implemented")
    }

    override fun resources(matcher: Matcher<ResourcePathed>): ResourceStoreNodeList? {
        TODO("Not yet implemented")
    }

    override fun temporaryFile(
        baseName: FileName,
        extension: Extension
    ): WritableResource {
        TODO("Not yet implemented")
    }

    override val store: ResourceStore
        get() = TODO("Not yet implemented")
    override val filename: Filename
        get() = TODO("Not yet implemented")
    override val size: Bytes
        get() = TODO("Not yet implemented")
    override val createdAtUtc: Instant
        get() = TODO("Not yet implemented")
    override val lastModifiedAtUtc: Instant
        get() = TODO("Not yet implemented")
    override val lastAccessedAtUtc: Instant
        get() = TODO("Not yet implemented")
    override val path: Path
        get() = TODO("Not yet implemented")
    override val uri: URI
        get() = TODO("Not yet implemented")
    override val scheme: ResourceScheme
        get() = TODO("Not yet implemented")
    override val proximity: ResourceProximity
        get() = TODO("Not yet implemented")
    override val isReadable: Boolean
        get() = TODO("Not yet implemented")

    override fun rename(target: Resource) {
        TODO("Not yet implemented")
    }
}
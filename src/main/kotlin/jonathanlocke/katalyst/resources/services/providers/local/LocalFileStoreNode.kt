package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.metadata.ResourceMetadataValue.*
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import java.nio.file.Files
import java.nio.file.Files.readAttributes
import java.nio.file.attribute.BasicFileAttributes

abstract class LocalFileStoreNode(override val location: ResourceLocation) : ResourceNodeService {

    override fun metadata() = tryValue {
        val attributes = readAttributes(location.path, BasicFileAttributes::class.java)
        ResourceMetadata(
            setOf(HasSize, HasCreatedAt, HasLastAccessedAt, HasLastModifiedAt),
            bytes(Files.size(location.path)),
            attributes.lastModifiedTime().toInstant(),
            attributes.lastAccessTime().toInstant(),
            attributes.creationTime().toInstant()
        )
    }

    override fun exists() = tryBoolean {
        Files.exists(location.path)
    }

    override fun delete() = tryBoolean {
        Files.deleteIfExists(location.path)
    }

    override fun moveTo(target: ResourceLocation) = tryBoolean {
        requireOrFail(store.contains(location), "Target is not in store '$store': $target")
        Files.move(location.path, location.path) != null
    }
}

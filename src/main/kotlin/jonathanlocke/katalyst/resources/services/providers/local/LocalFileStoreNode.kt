package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import java.nio.file.Files
import java.nio.file.Files.readAttributes
import java.nio.file.attribute.BasicFileAttributes

abstract class LocalFileStoreNode(override val location: ResourceLocation) : ResourceNodeService, ProblemSourceMixin {

    override val metadata: ResourceMetadata
        get() {
            val attributes = readAttributes(location.path, BasicFileAttributes::class.java)
            return ResourceMetadata(
                bytes(Files.size(location.path)),
                attributes.lastModifiedTime().toInstant(),
                attributes.lastAccessTime().toInstant(),
                attributes.creationTime().toInstant()
            )
        }

    override fun exists() = tryBoolean("Could not check existence of $location.path") {
        Files.exists(location.path)
    }

    override fun delete() = tryBoolean("Could not delete $location.path") {
        Files.deleteIfExists(
            location.path
        )
    }

    override fun moveTo(target: ResourceLocation): Boolean {
        requireOrFail(store.contains(location), "Target is not in store '$store': $target")
        return Files.move(location.path, location.path) != null
    }
}

package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.ResourceCapability
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Delete
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Move
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Write
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.services.ResourceNodeService
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes

abstract class LocalFileSystemNode(override val location: ResourceLocation) : ResourceNodeService, ProblemSourceMixin {

    override val metadata: ResourceMetadata
        get() {
            val attributes = attributes()
            return ResourceMetadata(
                bytes(Files.size(location.path)),
                attributes.lastModifiedTime().toInstant(),
                attributes.lastAccessTime().toInstant(),
                attributes.creationTime().toInstant()
            )
        }

    override fun can(capability: ResourceCapability): Boolean {
        return when (capability) {
            Resolve -> Files.exists(location.path)
            Delete -> true
            Move -> true
            Read -> Files.isReadable(location.path)
            Write -> Files.isWritable(location.path)
            else -> false
        }
    }

    fun delete() = tryCatch(this) { Files.deleteIfExists(location.path) }

    fun moveTo(target: ResourceLocation): Boolean {
        requireOrFail(store.contains(location), "Target is not in store '$store': $target")
        requireOrFail(can(Read), "Cannot read from: $location")
        requireOrFail(store.resource(target).can(Write), "Cannot write to: $location")
        return Files.move(location.path, location.path) != null
    }

    private fun attributes(): BasicFileAttributes = Files.readAttributes(location.path, BasicFileAttributes::class.java)
}

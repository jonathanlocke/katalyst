package jonathanlocke.katalyst.resources.services.providers.classpath

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.toBytes
import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.progress.ProgressReporter.Companion.nullProgressReporter
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.metadata.ResourceMetadata
import jonathanlocke.katalyst.resources.metadata.ResourceMetadataValue.HasLastModifiedAt
import jonathanlocke.katalyst.resources.metadata.ResourceMetadataValue.HasSize
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.streaming.io.ResourceInputStream
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import java.time.Instant

class ClassPathResource(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : ClassPathNode(location), ResourceService {

    override val capabilities = setOf(Resolve, Read)

    override fun isWritable() = false
    override fun moveTo(target: ResourceLocation) = throw unimplemented()
    override fun delete() = throw unimplemented()
    override fun exists() = isReadable()

    override fun metadata(): ResourceMetadata? = tryValue {
        val resource = scanResource()!!
        ResourceMetadata(
            setOf(HasSize, HasLastModifiedAt),
            resource.length.toBytes(),
            Instant.ofEpochMilli(resource.lastModified)
        )
    }

    override fun openForReading(progressReporter: ProgressReporter): ResourceInputStream {
        val input = Thread.currentThread().contextClassLoader.getResourceAsStream(location.path.toString())
        if (input != null) {
            return ResourceInputStream(progressReporter, input.buffered())
        }
        throw failure("Could not find classpath resource: $location")
    }

    override fun openForWriting(mode: WriteMode, progressReporter: ProgressReporter): ResourceOutputStream {
        throw failure("Cannot write to a classpath resource")
    }

    override fun isReadable(): Boolean = try {
        openForReading(nullProgressReporter)
        true
    } catch (_: Exception) {
        false
    }
}

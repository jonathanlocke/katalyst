package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Delete
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.GetMetadata
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Move
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Resolve
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Write
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.streaming.io.ResourceInputStream
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import java.nio.file.Files

class LocalFile(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : LocalFileStoreNode(location), ResourceService {

    override val capabilities = setOf(Resolve, GetMetadata, Move, Delete, Read, Write)

    override fun openForReading(progressReporter: ProgressReporter): ResourceInputStream {
        requireOrError(isReadable(), "Cannot read from: $location")
        return ResourceInputStream(progressReporter, Files.newInputStream(location.path).buffered())
    }

    override fun openForWriting(mode: WriteMode, progressReporter: ProgressReporter): ResourceOutputStream {
        requireOrError(isWritable(), "Cannot write to: $location")
        return ResourceOutputStream(progressReporter, Files.newOutputStream(location.path).buffered())
    }

    override fun isReadable() = tryBoolean("Could not determine readability of $location") {
        Files.isReadable(location.path)
    }

    override fun isWritable() = tryBoolean("Could not determine writability of $location") {
        Files.isWritable(location.path)
    }
}

package jonathanlocke.katalyst.resources.services.providers.local

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.ResourceCapability.Companion.Write
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

    override fun openForReading(progressReporter: ProgressReporter): ResourceInputStream {
        requireOrFail(can(Read), "Cannot write to: $location")
        return ResourceInputStream(progressReporter, Files.newInputStream(location.path).buffered())
    }

    override fun openForWriting(mode: WriteMode, progressReporter: ProgressReporter): ResourceOutputStream {
        requireOrFail(can(Write), "Cannot write to: $location")
        return ResourceOutputStream(progressReporter, Files.newOutputStream(location.path).buffered())
    }
}

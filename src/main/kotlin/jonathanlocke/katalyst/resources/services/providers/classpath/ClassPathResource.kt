package jonathanlocke.katalyst.resources.services.providers.classpath

import jonathanlocke.katalyst.progress.ProgressReporter
import jonathanlocke.katalyst.resources.capabilities.ResourceCapability.Companion.Read
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceService
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.services.providers.local.LocalFileStoreNode
import jonathanlocke.katalyst.resources.streaming.io.ResourceInputStream
import jonathanlocke.katalyst.resources.streaming.io.ResourceOutputStream
import jonathanlocke.katalyst.resources.streaming.io.WriteMode
import java.nio.file.Files

class ClassPathResource(
    override val store: ResourceStoreService,
    override val location: ResourceLocation,
) : LocalFileStoreNode(location), ResourceService {

    override fun openForReading(progressReporter: ProgressReporter): ResourceInputStream =
        ResourceInputStream(progressReporter, Files.newInputStream(location.path).buffered())

    override fun isReadable() = tryBoolean("") {
        openForReading(ProgressReporter.nullProgressReporter)
    }

    override fun isWritable() = false

    override val capabilities = setOf(Read)

    override fun openForWriting(mode: WriteMode, progressReporter: ProgressReporter): ResourceOutputStream {
        throw UnsupportedOperationException("Cannot write to a classpath resource.")
    }
}

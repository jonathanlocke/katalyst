package jonathanlocke.katalyst.resources

import jonathanlocke.katalyst.resources.streams.WriteMode
import java.io.InputStream
import java.io.OutputStream

abstract class ResourceBase : Resource {

    final override fun openForReading(): InputStream = onOpenForReading()
    final override fun openForWriting(mode: WriteMode): OutputStream = onOpenForWriting(mode)

    abstract fun onOpenForReading(): InputStream
    abstract fun onOpenForWriting(mode: WriteMode): OutputStream
}
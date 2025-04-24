package jonathanlocke.katalyst.resources.streams

import jonathanlocke.katalyst.resources.streams.writing.WriteMode
import java.io.InputStream
import java.io.OutputStream

abstract class ResourceBase : Resource {

    final override fun openForReading(): InputStream = onOpenForReading()

    abstract fun onOpenForReading(): InputStream

    final override fun openForWriting(mode: WriteMode): OutputStream = onOpenForWriting(mode)

    abstract fun onOpenForWriting(mode: WriteMode): OutputStream
}
package jonathanlocke.katalyst.flux.resources.streams.io

import jonathanlocke.katalyst.flux.resources.streams.io.WriteMode.DoNotOverwrite
import java.io.OutputStream

interface StreamWritable {

    fun openForWriting(mode: WriteMode = DoNotOverwrite): OutputStream
}

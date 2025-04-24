package jonathanlocke.katalyst.resources.streams.writing

import jonathanlocke.katalyst.resources.streams.writing.WriteMode.DoNotOverwrite
import java.io.OutputStream

interface StreamWritable {

    fun openForWriting(mode: WriteMode = DoNotOverwrite): OutputStream
}

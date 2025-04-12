package jonathanlocke.katalyst.flux.io

import jonathanlocke.katalyst.flux.io.WriteMode.*
import java.io.OutputStream

interface StreamWritable {

    fun openForWriting(mode: WriteMode = DoNotOverwrite): OutputStream
}

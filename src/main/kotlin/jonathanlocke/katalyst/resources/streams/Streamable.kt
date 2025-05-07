package jonathanlocke.katalyst.resources.streams

import jonathanlocke.katalyst.resources.streams.WriteMode.DoNotOverwrite
import java.io.InputStream
import java.io.OutputStream

interface Streamable : Copyable {

    fun openForReading(): InputStream
    fun openForWriting(mode: WriteMode = DoNotOverwrite): OutputStream
    fun streamer(): Streamer = Streamer(this)
}

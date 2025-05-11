package jonathanlocke.katalyst.resources.streaming

import jonathanlocke.katalyst.resources.streaming.WriteMode.DoNotOverwrite
import java.io.InputStream
import java.io.OutputStream

interface Streamable {

    fun openForReading(): InputStream
    fun openForWriting(mode: WriteMode = DoNotOverwrite): OutputStream
    fun streamer(): Streamer = Streamer(this)
}

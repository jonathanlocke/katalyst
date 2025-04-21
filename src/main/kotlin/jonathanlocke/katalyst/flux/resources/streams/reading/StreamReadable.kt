package jonathanlocke.katalyst.flux.resources.streams.reading

import java.io.InputStream

interface StreamReadable {

    fun openForReading(): InputStream
}
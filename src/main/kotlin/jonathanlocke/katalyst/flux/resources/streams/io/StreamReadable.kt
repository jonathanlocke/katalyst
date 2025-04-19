package jonathanlocke.katalyst.flux.resources.streams.io

import java.io.InputStream

interface StreamReadable {

    fun openForReading(): InputStream
}
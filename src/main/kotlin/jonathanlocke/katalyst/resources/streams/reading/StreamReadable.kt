package jonathanlocke.katalyst.resources.streams.reading

import java.io.InputStream

interface StreamReadable {

    fun openForReading(): InputStream
}
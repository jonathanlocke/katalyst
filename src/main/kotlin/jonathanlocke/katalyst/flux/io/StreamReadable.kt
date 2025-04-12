package jonathanlocke.katalyst.flux.io

import java.io.InputStream

interface StreamReadable {

    fun openForReading(): InputStream
}
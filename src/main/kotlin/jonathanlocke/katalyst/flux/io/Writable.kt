package jonathanlocke.katalyst.flux.io

import jonathanlocke.katalyst.flux.io.WriteMode.*

interface Writable {
    fun writer(mode: WriteMode = DoNotOverwrite): ResourceWriter
}

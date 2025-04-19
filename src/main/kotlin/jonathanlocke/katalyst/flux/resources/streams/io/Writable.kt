package jonathanlocke.katalyst.flux.resources.streams.io

import jonathanlocke.katalyst.flux.resources.streams.io.WriteMode.DoNotOverwrite

interface Writable {
    fun writer(mode: WriteMode = DoNotOverwrite): ResourceWriter
}

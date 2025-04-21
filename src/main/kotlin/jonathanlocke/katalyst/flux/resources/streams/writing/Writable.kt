package jonathanlocke.katalyst.flux.resources.streams.writing

import jonathanlocke.katalyst.flux.resources.streams.writing.WriteMode.DoNotOverwrite

interface Writable {
    fun writer(mode: WriteMode = DoNotOverwrite): ResourceWriter
}

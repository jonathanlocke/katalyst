package jonathanlocke.katalyst.resources.streams.writing

import jonathanlocke.katalyst.resources.streams.writing.WriteMode.DoNotOverwrite

interface Writable {
    fun writer(mode: WriteMode = DoNotOverwrite): ResourceWriter
}

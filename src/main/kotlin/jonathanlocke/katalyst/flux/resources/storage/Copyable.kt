package jonathanlocke.katalyst.flux.resources.storage

import jonathanlocke.katalyst.flux.resources.storage.Copyable.CopyMethod.Safe
import jonathanlocke.katalyst.flux.resources.streams.Resource
import jonathanlocke.katalyst.flux.resources.streams.writing.WriteMode
import jonathanlocke.katalyst.flux.resources.streams.writing.WriteMode.DoNotOverwrite

interface Copyable {

    enum class CopyMethod {
        Simple,
        Safe
    }

    fun copyTo(
        target: Resource,
        method: CopyMethod = Safe,
        mode: WriteMode = DoNotOverwrite
    )
}

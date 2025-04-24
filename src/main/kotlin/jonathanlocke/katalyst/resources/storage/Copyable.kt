package jonathanlocke.katalyst.resources.storage

import jonathanlocke.katalyst.resources.storage.Copyable.CopyMethod.Safe
import jonathanlocke.katalyst.resources.streams.Resource
import jonathanlocke.katalyst.resources.streams.writing.WriteMode
import jonathanlocke.katalyst.resources.streams.writing.WriteMode.DoNotOverwrite

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

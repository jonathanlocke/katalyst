package jonathanlocke.katalyst.flux.storage

import jonathanlocke.katalyst.flux.Resource
import jonathanlocke.katalyst.flux.io.WriteMode
import jonathanlocke.katalyst.flux.io.WriteMode.DoNotOverwrite
import jonathanlocke.katalyst.flux.storage.Copyable.CopyMethod.Safe

interface Copyable {

    enum class CopyMethod {
        Simple,
        Safe
    }

    fun copyTo(target: Resource, method: CopyMethod = Safe, mode: WriteMode = DoNotOverwrite)
}

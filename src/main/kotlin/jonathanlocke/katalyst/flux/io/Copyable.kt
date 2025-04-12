package jonathanlocke.katalyst.flux.io

import jonathanlocke.katalyst.flux.Resource
import jonathanlocke.katalyst.flux.io.Copyable.CopyMethod.*
import jonathanlocke.katalyst.flux.io.WriteMode.*

interface Copyable {

    enum class CopyMethod {
        Simple,
        Safe
    }

    fun copyTo(target: Resource, method: CopyMethod = Safe, mode: WriteMode = DoNotOverwrite)
}

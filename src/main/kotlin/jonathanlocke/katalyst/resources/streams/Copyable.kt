package jonathanlocke.katalyst.resources.streams

import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.streams.Copyable.CopyMethod.CopyAndRename
import jonathanlocke.katalyst.resources.streams.WriteMode.DoNotOverwrite

interface Copyable {

    enum class CopyMethod {
        Copy,
        CopyAndRename
    }

    fun copyTo(
        target: Resource,
        method: CopyMethod = CopyAndRename,
        mode: WriteMode = DoNotOverwrite
    )
}
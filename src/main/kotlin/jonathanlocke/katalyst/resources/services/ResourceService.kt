package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.streaming.CopyMethod
import jonathanlocke.katalyst.resources.streaming.Streamable
import jonathanlocke.katalyst.resources.streaming.WriteMode

interface ResourceService : ResourceNodeService, Streamable {

    override fun isFolder() = false

    fun canOverwrite(mode: WriteMode): Boolean
    fun renameTo(target: ResourceService): Boolean
    fun copyTo(target: ResourceService, method: CopyMethod, mode: WriteMode)
}
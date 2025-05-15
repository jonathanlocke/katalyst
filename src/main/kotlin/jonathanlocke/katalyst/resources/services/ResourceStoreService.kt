package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.problems.ProblemSource
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceProximity
import jonathanlocke.katalyst.resources.location.path.Filename

interface ResourceStoreService : ProblemSource {

    val root: ResourceLocation
    val proximity: ResourceProximity
    val size: Bytes
    val usable: Bytes
    val free: Bytes
    val percentFree: Percent get() = free.percentOf(size)
    val percentUsable: Percent get() = usable.percentOf(size)

    fun contains(location: ResourceLocation) = location.isUnder(root)

    fun resource(location: ResourceLocation): ResourceService
    fun folder(location: ResourceLocation): ResourceFolderService
    fun node(location: ResourceLocation): ResourceNodeService

    fun temporaryResourceLocation(baseName: Filename): ResourceLocation
    fun temporaryFolderLocation(baseName: Filename): ResourceLocation
}

package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.problems.ProblemSource
import jonathanlocke.katalyst.resources.capabilities.ResourceStoreCapability
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceProximity
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.metadata.ResourceStoreMetadata

interface ResourceStoreService : ProblemSource {

    val root: ResourceLocation
    val proximity: ResourceProximity
    val capabilities: Set<ResourceStoreCapability>

    fun metadata(): ResourceStoreMetadata

    fun contains(location: ResourceLocation) = location.isUnder(root)

    fun resource(location: ResourceLocation): ResourceService
    fun folder(location: ResourceLocation): ResourceFolderService
    fun node(location: ResourceLocation): ResourceNodeService

    fun temporaryResourceLocation(baseName: Filename): ResourceLocation
    fun temporaryFolderLocation(baseName: Filename): ResourceLocation
}

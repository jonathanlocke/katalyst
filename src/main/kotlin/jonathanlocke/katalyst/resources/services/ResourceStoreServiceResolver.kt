package jonathanlocke.katalyst.resources.services

import jonathanlocke.katalyst.resources.location.ResourceLocation

interface ResourceStoreServiceResolver {
    fun resolve(location: ResourceLocation): ResourceStoreService
}

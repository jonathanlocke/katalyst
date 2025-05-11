package jonathanlocke.katalyst.resources.services.resolvers

import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.location.ResourceScheme
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.services.ResourceStoreServiceResolver
import jonathanlocke.katalyst.resources.services.providers.local.LocalFileSystem
import java.net.URI
import java.nio.file.FileSystems

class ResourceStoreServiceRegistry : ResourceStoreServiceResolver, ProblemSourceMixin {

    private val services = HashMap<ResourceScheme, ResourceStoreService>()

    companion object {

        val resourceStoreServiceRegistry = ResourceStoreServiceRegistry()
    }

    init {
        FileSystems.getDefault().fileStores.forEach { store ->
            val root = URI.create("file:///${store.name()}")
            register(LocalFileSystem(ResourceLocation(root)))
        }
    }

    fun register(service: ResourceStoreService) {
        services.put(service.scheme, service)
    }

    override fun resolve(location: ResourceLocation): ResourceStoreService {
        val service = services[location.scheme]
        requireValue(service, "Location service $location is not registered")
        return service!!
    }
}
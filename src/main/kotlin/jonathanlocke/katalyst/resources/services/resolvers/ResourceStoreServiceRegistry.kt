package jonathanlocke.katalyst.resources.services.resolvers

import jonathanlocke.katalyst.problems.ProblemSourceMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.services.ResourceStoreServiceResolver
import jonathanlocke.katalyst.resources.services.providers.local.LocalFileStore
import java.net.URI
import java.nio.file.FileSystems

class ResourceStoreServiceRegistry : ResourceStoreServiceResolver, ProblemSourceMixin {

    private val services = HashMap<ResourceLocation, ResourceStoreService>()

    companion object {

        val resourceStoreServiceRegistry = ResourceStoreServiceRegistry()
    }

    init {

        // For each local file store,
        FileSystems.getDefault().fileStores.forEach { store ->

            // create the root location of the store,
            val root = ResourceLocation(URI.create("file:///${store.name()}"))

            // and register it as a local filesystem.
            register(LocalFileStore(root))
        }
    }

    fun register(service: ResourceStoreService) {
        services.put(service.root, service)
    }

    override fun resolve(location: ResourceLocation): ResourceStoreService {
        val service = services[location.root]
        requireOrFail(service, "Location service $location is not registered")
        return service!!
    }
}
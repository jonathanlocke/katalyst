package jonathanlocke.katalyst.resources.services.resolvers

import jonathanlocke.katalyst.resources.location.ResourceLocation
import jonathanlocke.katalyst.resources.services.ResourceStoreService
import jonathanlocke.katalyst.resources.services.ResourceStoreServiceResolver
import jonathanlocke.katalyst.resources.services.providers.classpath.ClassPathResourceStore
import jonathanlocke.katalyst.resources.services.providers.local.LocalFileStore
import jonathanlocke.katalyst.status.StatusHandlerMixin
import java.io.File
import java.net.URI

class ResourceStoreServiceRegistry : ResourceStoreServiceResolver, StatusHandlerMixin {

    private val services = HashMap<ResourceLocation, ResourceStoreService>()

    companion object {

        val resourceStoreServiceRegistry = ResourceStoreServiceRegistry()
    }

    init {

        // For each local file store,
        File.listRoots().forEach { root ->

            // register it as a local filesystem.
            register(LocalFileStore(ResourceLocation(URI.create("file://$root"))))
        }

        register(ClassPathResourceStore(ResourceLocation(URI.create("classpath:///"))))
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

package jonathanlocke.katalyst.resources.services.providers.classpath

import io.github.classgraph.ClassGraph
import io.github.classgraph.Resource
import jonathanlocke.katalyst.resources.location.ResourceLocation

open class ClassPathNode(
    open val location: ResourceLocation,
) {
    internal fun scanResource(): Resource? =
        ClassGraph().acceptPaths(basePath()).scan().use { scan -> scan.allResources.firstOrNull() }

    internal fun scanChildren(resolver: (ResourceLocation) -> ResourceLocation?): List<ResourceLocation> {

        val resources = mutableListOf<ResourceLocation>()

        // Scan the classpath
        ClassGraph().acceptPaths(basePath()).scan().use { scan ->

            // and for each resource in the scan,
            scan.allResources.forEach { resource ->

                // get the path relative to the base path,
                val relativePath = relativePath(resource.path)

                // resolve the relative path and add the resulting location to the list of resources.
                val location = ResourceLocation(location.uri.resolve(relativePath).normalize())
                val resolved = resolver.invoke(location)
                if (resolved != null) {
                    resources.add(location)
                }
            }
        }

        return resources
    }

    private fun toUnixPath(path: String) = path.replace("\\", "/")

    private fun relativePath(path: String) = toUnixPath(path.removePrefix(basePath()).trim('/'))

    /**
     * Returns the location of this classpath folder without any trailing slashes. This is required because
     * ClassGraph will not scan resources if the folder path ends with a slash.
     */
    private fun basePath(): String = toUnixPath(location.path.toString()).trim('/')
}

package jonathanlocke.katalyst.resources.creation

import jonathanlocke.katalyst.resources.storage.location.ResourceScheme
import java.net.URI

interface UriResourceResolver {
    fun isFolder(uri: URI): Boolean
    fun resolver(uri: URI): UriResourceResolver
    fun scheme(): ResourceScheme
}

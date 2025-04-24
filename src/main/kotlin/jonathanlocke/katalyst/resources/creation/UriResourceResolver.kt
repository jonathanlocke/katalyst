package jonathanlocke.katalyst.resources.creation

import java.net.URI

interface UriResourceResolver {
    fun isFolder(uri: URI): Boolean
    fun resolver(uri: URI): UriResourceResolver
    fun scheme(): ResourceScheme
}

package jonathanlocke.katalyst.flux.resources.uri

import java.net.URI

interface UriResourceResolver {
    fun isFolder(uri: URI): Boolean
    fun resolver(uri: URI): UriResourceResolver
    fun scheme(): ResourceScheme
}

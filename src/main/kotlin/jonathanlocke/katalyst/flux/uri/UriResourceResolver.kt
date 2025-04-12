package jonathanlocke.katalyst.flux.uri

import java.net.URI

interface UriResourceResolver {
    fun isFolder(uri: URI): Boolean
    fun resolver(uri: URI): UriResourceResolver
    fun scheme(): ResourceScheme
}

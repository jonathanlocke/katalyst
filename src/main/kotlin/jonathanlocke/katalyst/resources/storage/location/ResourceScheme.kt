package jonathanlocke.katalyst.resources.storage.location

import java.net.URI

class ResourceScheme(val scheme: String) {

    fun matches(uri: URI): Boolean {
        return uri.scheme == scheme
    }
}
package jonathanlocke.katalyst.resources.creation

import java.net.URI

class ResourceScheme(val scheme: String) {

    fun matches(uri: URI): Boolean {
        return uri.scheme == scheme
    }
}

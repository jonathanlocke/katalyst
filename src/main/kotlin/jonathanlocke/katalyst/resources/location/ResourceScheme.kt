package jonathanlocke.katalyst.resources.location

import java.net.URI

class ResourceScheme(val scheme: String) {

    constructor(uri: URI) : this(uri.scheme)

    fun matches(uri: URI): Boolean {
        return uri.scheme == scheme
    }

    override fun equals(other: Any?): Boolean = other is ResourceScheme && other.scheme == scheme
    override fun hashCode(): Int = scheme.hashCode()
}
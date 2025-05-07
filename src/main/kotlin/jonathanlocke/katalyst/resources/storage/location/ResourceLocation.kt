package jonathanlocke.katalyst.resources.storage.location

import jonathanlocke.katalyst.resources.ResourcePath
import jonathanlocke.katalyst.resources.storage.ResourceFolder
import java.net.URI

interface ResourceLocation {

    val uri: URI
    val path: ResourcePath
    val scheme: ResourceScheme

    fun folder(): ResourceFolder
    fun parent(): ResourceFolder
}
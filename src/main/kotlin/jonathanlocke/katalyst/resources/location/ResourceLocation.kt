package jonathanlocke.katalyst.resources.location

import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.location.path.Paths.Companion.filename
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.name

class ResourceLocation(val uri: URI) {

    val root: ResourceLocation get() = ResourceLocation(uri.resolve(rootPath.toString()))
    val parent: ResourceLocation? get() = path.parent?.let { ResourceLocation(uri.resolve(it.toString())) }
    val rootPath: Path get() = path.root ?: Path.of(separator)
    val path: Path get() = Path.of(uri.path)
    val filename: Filename get() = path.filename()
    val separator: String get() = path.fileSystem.separator
    val scheme: ResourceScheme get() = ResourceScheme(uri)

    fun child(filename: Filename): ResourceLocation = ResourceLocation(uri.resolve(filename.name))
}
package jonathanlocke.katalyst.resources.location

import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.location.path.Paths.Companion.filename
import jonathanlocke.katalyst.resources.location.path.Paths.Companion.isRoot
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.relativeTo

open class ResourceLocation(open val uri: URI) {

    constructor(path: Path) : this(path.toUri())

    val root: ResourceLocation get() = ResourceLocation(uri.resolve(rootPath.toString()))
    val parent: ResourceLocation? get() = path.parent?.let { ResourceLocation(uri.resolve(it.toString())) }
    val rootPath: Path get() = path.root ?: Path.of(separator)
    val path: Path get() = Path.of(uri.path)
    val filename: Filename get() = path.filename()
    val separator: String get() = path.fileSystem.separator
    val scheme: ResourceScheme get() = ResourceScheme(uri)
    val isAbsolute = path.isAbsolute
    val isRoot = path.isRoot()
    val isRelative = !isAbsolute
    fun child(relativeLocation: ResourceLocation) = ResourceLocation(uri.resolve(relativeLocation.toString()))
    fun child(filename: Filename) = child(ResourceLocation(filename))
    fun relativeTo(folder: ResourceLocation) = ResourceLocation(uri.resolve(path.relativeTo(folder.path).toString()))
    fun isUnder(location: ResourceLocation) = location.uri.path.startsWith(uri.path)
}

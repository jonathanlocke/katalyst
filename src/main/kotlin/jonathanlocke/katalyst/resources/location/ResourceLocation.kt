package jonathanlocke.katalyst.resources.location

import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.ResourceFolder
import jonathanlocke.katalyst.resources.ResourceStore
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.location.path.Paths.filename
import jonathanlocke.katalyst.resources.location.path.Paths.isRoot
import jonathanlocke.katalyst.status.StatusHandler
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.relativeTo

open class ResourceLocation(open val uri: URI) {

    constructor(path: Path) : this(path.toUri())
    constructor(filename: Filename) : this(filename.path)
    constructor(location: String) : this(Path.of(location))

    companion object {
        fun Path.toResourceLocation() = ResourceLocation(this)
        fun Path.toResource(statusHandler: StatusHandler) = ResourceLocation(this).resource(statusHandler)
        fun Path.toFolder(statusHandler: StatusHandler) = ResourceLocation(this).folder(statusHandler)
        fun Path.toResourceStore(statusHandler: StatusHandler) = ResourceLocation(this).store(statusHandler)
        fun String.toResourceLocation() = ResourceLocation(this)
        fun String.toResource(statusHandler: StatusHandler) = ResourceLocation(this).resource(statusHandler)
        fun String.toFolder(statusHandler: StatusHandler) = ResourceLocation(this).folder(statusHandler)
        fun String.toResourceStore(statusHandler: StatusHandler) = ResourceLocation(this).store(statusHandler)
        fun URI.toResourceLocation() = ResourceLocation(this)
        fun URI.toResource(statusHandler: StatusHandler) = ResourceLocation(this).resource(statusHandler)
        fun URI.toFolder(statusHandler: StatusHandler) = ResourceLocation(this).folder(statusHandler)
        fun URI.toResourceStore(statusHandler: StatusHandler) = ResourceLocation(this).store(statusHandler)
    }

    val root: ResourceLocation get() = ResourceLocation(rootPath)
    val parent: ResourceLocation? get() = path.parent?.let { ResourceLocation(uri.resolve(it.toString())) }
    val rootPath: Path get() = path.root ?: Path.of(separator)
    val path: Path get() = Path.of(uri.path)
    val filename: Filename get() = path.filename()
    val separator: String get() = path.fileSystem.separator
    val scheme: ResourceScheme get() = ResourceScheme(uri)
    val isAbsolute = path.isAbsolute
    val isRoot = path.isRoot()
    val isRelative = !isAbsolute

    fun resource(statusHandler: StatusHandler) = Resource(statusHandler, this)
    fun folder(statusHandler: StatusHandler) = ResourceFolder(statusHandler, this)
    fun store(statusHandler: StatusHandler) = ResourceStore(statusHandler, this)

    fun resolve(location: ResourceLocation) = ResourceLocation(uri.resolve(location.toString()))
    fun resolve(location: String) = ResourceLocation(uri.resolve(location))
    fun resolve(filename: Filename) = resolve(ResourceLocation(filename))

    fun relativeTo(folder: ResourceLocation) = ResourceLocation(uri.resolve(path.relativeTo(folder.path).toString()))
    fun isUnder(location: ResourceLocation) = location.uri.path.startsWith(uri.path)

    override fun equals(other: Any?) = if (other is ResourceLocation) uri == other.uri else false
    override fun hashCode() = uri.hashCode()
    override fun toString() = uri.toString()
}

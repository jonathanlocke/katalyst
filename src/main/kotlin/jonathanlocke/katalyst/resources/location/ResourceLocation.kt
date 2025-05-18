package jonathanlocke.katalyst.resources.location

import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.resources.Resource
import jonathanlocke.katalyst.resources.ResourceFolder
import jonathanlocke.katalyst.resources.ResourceStore
import jonathanlocke.katalyst.resources.location.path.Filename
import jonathanlocke.katalyst.resources.location.path.Paths.Companion.filename
import jonathanlocke.katalyst.resources.location.path.Paths.Companion.isRoot
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.relativeTo

open class ResourceLocation(open val uri: URI) {

    constructor(path: Path) : this(path.toUri())
    constructor(filename: Filename) : this(filename.path)
    constructor(location: String) : this(URI.create(location))

    companion object {
        fun Path.toResourceLocation() = ResourceLocation(this)
        fun Path.toResource(problemHandler: ProblemHandler) = ResourceLocation(this).resource(problemHandler)
        fun Path.toFolder(problemHandler: ProblemHandler) = ResourceLocation(this).folder(problemHandler)
        fun Path.toResourceStore(problemHandler: ProblemHandler) = ResourceLocation(this).store(problemHandler)
        fun String.toResourceLocation() = ResourceLocation(this)
        fun String.toResource(problemHandler: ProblemHandler) = ResourceLocation(this).resource(problemHandler)
        fun String.toFolder(problemHandler: ProblemHandler) = ResourceLocation(this).folder(problemHandler)
        fun String.toResourceStore(problemHandler: ProblemHandler) = ResourceLocation(this).store(problemHandler)
        fun URI.toResourceLocation() = ResourceLocation(this)
        fun URI.toResource(problemHandler: ProblemHandler) = ResourceLocation(this).resource(problemHandler)
        fun URI.toFolder(problemHandler: ProblemHandler) = ResourceLocation(this).folder(problemHandler)
        fun URI.toResourceStore(problemHandler: ProblemHandler) = ResourceLocation(this).store(problemHandler)
    }

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

    fun resource(problemHandler: ProblemHandler) = Resource(problemHandler, this)
    fun folder(problemHandler: ProblemHandler) = ResourceFolder(problemHandler, this)
    fun store(problemHandler: ProblemHandler) = ResourceStore(problemHandler, this)

    fun resolve(location: ResourceLocation) = ResourceLocation(uri.resolve(location.toString()))
    fun resolve(location: String) = ResourceLocation(uri.resolve(location))
    fun resolve(filename: Filename) = resolve(ResourceLocation(filename))

    fun relativeTo(folder: ResourceLocation) = ResourceLocation(uri.resolve(path.relativeTo(folder.path).toString()))
    fun isUnder(location: ResourceLocation) = location.uri.path.startsWith(uri.path)
}

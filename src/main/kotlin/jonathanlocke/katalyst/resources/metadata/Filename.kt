package jonathanlocke.katalyst.resources.metadata

import jonathanlocke.katalyst.resources.metadata.Paths.Companion.extension
import java.io.File
import java.nio.file.Path
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.util.*
import java.util.function.Consumer
import kotlin.io.path.name

class Filename(private val path: Path) : Path by path {

    init {
        check(nameCount == 1) { "Filename cannot be a path with more than one component" }
    }

    val fullName = path.name
    val baseName = fullName.substringBeforeLast('.')
    val extension: Extension? = path.extension()
    val hasExtension = extension() != null
    val length = fullName.length

    override fun hashCode() = path.hashCode()
    override fun equals(other: Any?) = path == other
    override fun toString(): String = path.toString()

    fun withExtension(extension: Extension): Filename =
        Filename(Path.of("$this$extension"))

    fun withoutExtension(extension: Extension): Filename =
        Filename(Path.of(toString().removeSuffix("$extension")))

    fun withoutExtension(): Filename =
        parseFilename(baseName)

    /**
     * Manual delegation to [Path] is necessary here because the 'by' keyword in Kotlin doesn't work with
     * the default methods in [Path]
     */
    override fun startsWith(other: String): Boolean = path.startsWith(other)
    override fun endsWith(other: String): Boolean = path.endsWith(other)
    override fun resolve(other: String): Path = path.resolve(other)
    override fun resolve(first: Path?, vararg more: Path?): Path? = path.resolve(first, *more)
    override fun resolve(first: String?, vararg more: String?): Path? = path.resolve(first, *more)
    override fun resolveSibling(other: Path): Path = path.resolveSibling(other)
    override fun resolveSibling(other: String): Path = path.resolveSibling(other)
    override fun toFile(): File = path.toFile()
    override fun iterator(): MutableIterator<Path> = path.iterator()
    override fun forEach(action: Consumer<in Path>?) = path.forEach(action)
    override fun spliterator(): Spliterator<Path?> = path.spliterator()
    override fun register(
        watcher: WatchService,
        vararg events: WatchEvent.Kind<*>
    ): WatchKey = path.register(watcher, *events)

    companion object {

        fun parseFilename(path: Path): Filename =
            Filename(path)

        fun parseFilename(path: String): Filename =
            parseFilename(Path.of(path))
    }
}

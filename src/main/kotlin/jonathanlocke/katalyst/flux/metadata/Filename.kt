package jonathanlocke.katalyst.flux.metadata

import jonathanlocke.katalyst.flux.metadata.Paths.Companion.extension
import java.nio.file.Path
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

    fun withExtension(extension: Extension): Filename = Filename(Path.of("$this$extension"))
    fun withoutExtension(extension: Extension): Filename = Filename(Path.of(toString().removeSuffix("$extension")))
    fun withoutExtension(): Filename = parseFilename(baseName)

    companion object {

        fun parseFilename(path: Path): Filename = Filename(path)
        fun parseFilename(path: String): Filename = parseFilename(Path.of(path))
    }
}

package jonathanlocke.katalyst.resources.location.path

import jonathanlocke.katalyst.resources.location.path.Paths.Companion.extension
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers.Companion.throwOnError
import java.nio.file.Path
import kotlin.io.path.name

class Filename(val path: Path) : Path by path {

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

    companion object {

        fun parseFilename(path: Path, statusHandler: StatusHandler = throwOnError): Filename {
            statusHandler.requireOrFail(path.nameCount > 1, "Not a filename: $path")
            return Filename(path)
        }

        fun parseFilename(path: String): Filename =
            parseFilename(Path.of(path))
    }
}

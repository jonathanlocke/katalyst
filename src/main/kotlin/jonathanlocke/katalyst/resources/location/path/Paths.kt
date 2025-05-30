package jonathanlocke.katalyst.resources.location.path

import jonathanlocke.katalyst.status.StatusException
import java.io.File
import java.nio.file.Path
import kotlin.io.path.name

object Paths {

    fun Path.hasParent(): Boolean = parent != null
    fun Path.hasFileSyntax() = !hasFolderSyntax()
    fun Path.hasFolderSyntax() = toString().endsWith(File.separator)
    fun Path.withoutFirst() =
        subpath(1, nameCount) ?: StatusException.Companion.fail("Path '$this' must have a first")

    fun Path.withoutLast() =
        subpath(0, nameCount - 1) ?: StatusException.Companion.fail("Path '$this' must have a last")

    fun Path.filename(): Filename = Filename(this.fileName)
    fun Path.isRoot() = root != null && this == root
    fun Path.matches(pattern: Regex) = pattern.matches(toString())

    operator fun Path.plus(other: Path): Path = resolve(other)

    fun Path.extension(): Extension? {
        val extension = this.name.substringAfter('.', "")
        return if (extension.isEmpty()) null else Extension(
            extension
        )
    }

    fun Path.withoutLeading(prefix: Path): Path {
        require(this.startsWith(prefix)) { "Path '$this' does not start with prefix '$prefix'" }
        return this.subpath(prefix.nameCount, this.nameCount)
    }

    fun Path.withoutTrailing(suffix: Path): Path {
        require(this.endsWith(suffix)) { "Path '$this' does not end with suffix '$suffix'" }
        return this.subpath(0, this.nameCount - suffix.nameCount)
    }
}

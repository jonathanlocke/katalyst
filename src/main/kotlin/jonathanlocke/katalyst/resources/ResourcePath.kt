package jonathanlocke.katalyst.resources

import java.nio.file.Path

class ResourcePath(val path: Path) {

    companion object {
        fun Path.toResourcePath() = ResourcePath(this)
        fun String.toResourcePath() = ResourcePath(Path.of(this))
    }
}
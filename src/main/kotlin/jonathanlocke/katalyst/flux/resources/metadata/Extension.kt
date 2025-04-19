package jonathanlocke.katalyst.flux.resources.metadata

class Extension(text: String) {

    val extension: String = if (text.startsWith(".")) text else ".$text"
    val length = extension.length

    override fun equals(other: Any?): Boolean = other is Extension && other.extension == this.extension
    override fun hashCode(): Int = extension.hashCode()
    override fun toString(): String = extension

    companion object {

        val GIF: Extension =
            parseExtension(".gif")
        val JPEG: Extension =
            parseExtension(".jpeg")
        val JPG: Extension =
            parseExtension(".jpg")
        val PNG: Extension =
            parseExtension(".png")
        val TMP: Extension =
            parseExtension(".tmp")
        val TXT: Extension =
            parseExtension(".txt")
        val ZIP: Extension =
            parseExtension(".zip")
        val TAR: Extension =
            parseExtension(".tar")
        val GZ: Extension =
            parseExtension(".gz")

        fun parseExtension(extension: String): Extension {
            require(extension.startsWith("."))
            return Extension(extension)
        }
    }
}
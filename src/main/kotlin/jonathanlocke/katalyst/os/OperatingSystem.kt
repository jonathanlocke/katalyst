package jonathanlocke.katalyst.os

class OperatingSystem(val name: String, val type: Type) {

    enum class Type { WINDOWS, UNIX }

    fun environmentVariables(): Set<EnvironmentVariable> = System.getenv().map { EnvironmentVariable(it.key) }.toSet()

    companion object {

        val Windows = OperatingSystem("Windows", Type.WINDOWS)
        val Linux = OperatingSystem("Linux", Type.UNIX)
        val MacOS = OperatingSystem("Mac OS", Type.UNIX)
        val Unix = OperatingSystem("Unix", Type.UNIX)

        fun get(): OperatingSystem {
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> Windows
                os.contains("nux") -> Linux
                os.contains("mac") -> MacOS
                os.contains("nix") -> Unix
                else -> error("Unknown operating system: $os")
            }
        }
    }
}

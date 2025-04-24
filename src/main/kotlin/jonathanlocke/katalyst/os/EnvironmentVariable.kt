package jonathanlocke.katalyst.os

class EnvironmentVariable(val name: String, val description: String = "", val defaultValue: String = "") {
    val value: String = System.getenv(name) ?: defaultValue

    companion object {

        val USER = EnvironmentVariable("USER", "The user's username.")
        val HOME = EnvironmentVariable("HOME", "The user's home folder.")
        val PATH = EnvironmentVariable("PATH", "A list of folders containing executables.")
        val TERM = EnvironmentVariable("TERM", "The name of the terminal type to use.")
        val PWD = EnvironmentVariable("PWD", "The current working folder.")
        val LANG = EnvironmentVariable("LANG", "The default locale, specified as a language and a country.")
        val HOSTNAME = EnvironmentVariable("HOSTNAME", "The hostname of the machine.")
        val JAVA_HOME = EnvironmentVariable("JAVA_HOME", "The folder where the Java is installed.")
        val SHELL = EnvironmentVariable("SHELL", "The name of the shell to use.")
        val TMPDIR = EnvironmentVariable("TMPDIR", "The default temporary folder.")
    }
}

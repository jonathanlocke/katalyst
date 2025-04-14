package jonathanlocke.katalyst.nucleus.platform.os

class EnvironmentVariable(val name: String, val description: String = "", val defaultValue: String = "") {
    val value: String = System.getenv(name) ?: defaultValue
}

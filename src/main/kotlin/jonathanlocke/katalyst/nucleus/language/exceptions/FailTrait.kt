package jonathanlocke.katalyst.nucleus.language.exceptions

interface FailTrait {
    fun ensure(boolean: Boolean, message: String) = if (!boolean) throw Exception(message) else {
    }
}

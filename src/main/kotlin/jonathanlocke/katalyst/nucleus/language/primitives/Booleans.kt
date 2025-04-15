package jonathanlocke.katalyst.nucleus.language.primitives

class Booleans {

    fun Boolean.orThrow(message: String = "Error", throwable: Throwable) = this || throw Exception(message, throwable)

    fun Boolean.thenThrow(message: String = "Error", throwable: Throwable) =
        !this || throw Exception(message, throwable)

    fun Boolean.then(code: () -> Unit) = if (this) code() else Unit

    fun <T> Boolean.then(code: () -> T): T? = if (this) code() else null

    fun Boolean.whenTrue(code: () -> Unit): Boolean {
        if (this) {
            code()
        }
        return this
    }

    fun Boolean.whenFalse(code: () -> Unit): Boolean {
        if (!this) {
            code()
        }
        return this
    }
}

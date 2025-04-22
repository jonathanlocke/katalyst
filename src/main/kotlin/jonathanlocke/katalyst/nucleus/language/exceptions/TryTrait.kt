package jonathanlocke.katalyst.nucleus.language.exceptions

import java.util.function.Supplier

interface TryTrait {

    fun <T> tryDefault(defaultValue: T, code: Supplier<T>) = try {
        code.get()
    } catch (e: Exception) {
        defaultValue
    }
}
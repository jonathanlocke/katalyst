package jonathanlocke.katalyst.nucleus.language.errors

import jonathanlocke.katalyst.nucleus.language.errors.strategies.Throw
import jonathanlocke.katalyst.nucleus.mixins.Mixin

interface ErrorHandlerMixin<T> : Mixin {

    fun errorHandler(handler: ErrorHandler<T>? = null): ErrorHandler<T> {
        return mixinAttach { handler ?: Throw() }
    }

    fun error(message: String, throwable: Throwable? = null): T? {
        return errorHandler().error(message, throwable)
    }
}

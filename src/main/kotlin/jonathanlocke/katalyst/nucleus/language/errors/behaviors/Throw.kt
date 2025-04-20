package jonathanlocke.katalyst.nucleus.language.errors.behaviors

import jonathanlocke.katalyst.nucleus.language.errors.ErrorBehavior
import jonathanlocke.katalyst.nucleus.language.errors.Result

/**
 * Error handler that throws an exception
 *
 * ```
 * text.convert(ToInt(), errorHandler = Throw()))
 * ```
 *
 * Note: Throw() is the default, so explicitly setting the error handler in a conversion is not necessary.
 */
class Throw<Value> : ErrorBehavior<Value> {
    override fun error(message: String, value: Value?, throwable: Throwable?): Result<Value>? =
        throw Exception(message, throwable)
}
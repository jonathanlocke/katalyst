package jonathanlocke.katalyst.nucleus.language.errors.handlers

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

/**
 * Error handler that throws an exception
 *
 * ```
 * text.convert(ToInt(), errorHandler = Throw()))
 * ```
 *
 * Note: Throw() is the default, so explicitly setting the error handler in a conversion is not necessary.
 */
class Throw<T> : ErrorHandler<T> {
    override fun error(message: String, throwable: Throwable?): T = throw Exception(message, throwable)
}
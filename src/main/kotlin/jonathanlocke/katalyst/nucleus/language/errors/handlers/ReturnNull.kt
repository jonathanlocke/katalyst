package jonathanlocke.katalyst.nucleus.language.errors.handlers

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

/**
 * Error handler that returns null.
 *
 * ```
 * text.convert(ToInt(), ReturnNull()))
 * ```
 */
class ReturnNull<T> : ErrorHandler<T> {
    override fun error(message: String, value: T?, throwable: Throwable?): T? = null
}
package jonathanlocke.katalyst.nucleus.language.errors.handlers

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

/**
 * Error handler that returns the given default value
 *
 * ```
 * text.convert(ToInt(), returnDefault(7)))
 * ```
 */
class ReturnDefault<T>(private val defaultValue: T) : ErrorHandler<T> {
    override fun error(message: String, throwable: Throwable?, value: T?): T = defaultValue
}
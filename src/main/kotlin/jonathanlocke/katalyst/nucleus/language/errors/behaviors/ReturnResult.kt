package jonathanlocke.katalyst.nucleus.language.errors.behaviors

import jonathanlocke.katalyst.nucleus.language.errors.ErrorBehavior
import jonathanlocke.katalyst.nucleus.language.errors.Problem
import jonathanlocke.katalyst.nucleus.language.errors.Result

/**
 * Error handler that returns null.
 *
 * ```
 * text.convert(ToInt(), ReturnNull()))
 * ```
 */
class ReturnResult<Value> : ErrorBehavior<Value> {
    override fun error(message: String, value: Value?, throwable: Throwable?): Result<Value>? =
        Result(value, Problem(message, throwable, value))
}
package jonathanlocke.katalyst.nucleus.language.errors

import jonathanlocke.katalyst.nucleus.language.errors.handlers.Throw
import jonathanlocke.katalyst.nucleus.mixins.Mixin

/**
 * A mixin that attaches an [ErrorHandler] to any object via [errorHandler]. The default method [error] uses the
 * attached error handler to report errors.
 *
 *  - [errorHandler] - Sets the error handler to use when reporting errors
 *  - [error] - Reports an error
 */
interface ErrorHandlerMixin<T> : Mixin {

    /**
     * Sets the error handler for this object
     */
    fun errorHandler(handler: ErrorHandler<T>? = null): ErrorHandler<T> = mixinAttach { handler ?: Throw() }

    /**
     * Invokes the error handler for this object with the given message
     * @param message The error message
     * @param throwable Any exception that caused the error
     */
    fun error(message: String, throwable: Throwable? = null): T? = errorHandler().error(message, throwable)
}

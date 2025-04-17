package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlerMixin

/**
 * A converter converts from one type ([From]) to another ([To]). Converters are [ErrorHandlerMixin]s, that report
 * errors to an interested party through an [ErrorHandler].
 *
 * **Conversions**
 *
 * - [convert] - Converts from the [From] type to the [To] type
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 *
 * **Errors**
 *
 *  - [errorHandler] - Sets the error handler to use when reporting errors
 *  - [error] - Reports an error
 *
 * @param From Source type
 * @param To Destination type
 * @see ErrorHandlerMixin
 */
interface Converter<From : Any, To : Any> : ErrorHandlerMixin<To> {
    
    /**
     * Converts the given [From] value to the [To] type. If an error occurs, the [ErrorHandler] associated
     * with the converter via the [ErrorHandlerMixin] will determine whether a null value is returned or an
     * exception is thrown.
     */
    fun convert(from: From?): To?

    /**
     * The value to use for conversions that result in a null [To] value. Overriding this method allows failed
     * conversions to provide a useful value. For example, if a string conversion fails, [nullValue] could return
     * a user-friendly string explaining that the conversion failed.
     */
    fun nullValue(): To? = null
}

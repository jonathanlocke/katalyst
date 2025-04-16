package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlerMixin

/**
 * A converter converts from one type to another. Converters are [ErrorHandlerMixin]s, that report errors to an
 * interested party through an [ErrorHandler].
 *
 * **Conversions**
 *
 * - [convert]
 * - [convertOrDefault]
 *
 * @param From Source type
 * @param To Destination type
 * @see ErrorHandlerMixin
 */
interface Converter<From : Any, To : Any> : ErrorHandler<To> {

    fun errorHandler(errorHandler: ErrorHandler<To>): Converter<From, To>
    fun convert(from: From?): To?
    fun nullValue(): To? = null
}

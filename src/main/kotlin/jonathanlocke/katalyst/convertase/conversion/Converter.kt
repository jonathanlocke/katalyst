package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlerMixin
import jonathanlocke.katalyst.nucleus.language.errors.handlers.ReturnNull
import jonathanlocke.katalyst.nucleus.language.errors.handlers.Throw
import kotlin.reflect.KClass

/**
 * A converter converts from a [From] type to a [To] type.
 *
 * **Conversions**
 *
 * - [convert] - Converts the given [From] value to a [To] value. If an error occurs, the [ErrorHandler] is called,
 *               which determines whether a null value is returned ([ReturnNull]) or an exception is thrown ([Throw]).
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 *
 * @param From Source type
 * @param To Destination type
 * @see ErrorHandlerMixin
 */
interface Converter<From : Any, To : Any> {

    /**
     * The type of the [From] class (necessary due to type erasure).
     */
    val fromClass: KClass<From>

    /**
     * The type of the [To] class (necessary due to type erasure).
     */
    val toClass: KClass<To>

    /**
     * Converts the given [From] value to a [To] value. If an error occurs, the [ErrorHandler] is called and
     * that determines whether a null value is returned ([ReturnNull]) or an exception is thrown ([Throw]).
     * @param from The value to convert
     * @param errorHandler The error handler to use when the conversion fails. Defaults to [Throw] if not provided.
     * @return Returns [To] if the conversion succeeded. If the conversion failed, returns null unless the error handler
     * throws an exception
     * @throws Exception Thrown by error handler if the conversion fails and the errorHandler is [Throw]
     *
     * @see ErrorHandler
     * @see ReturnNull
     * @see Throw
     */
    fun convert(from: From?, errorHandler: ErrorHandler<To?> = Throw()): To?

    /**
     * The value to use for conversions that result in a null [To] value. Overriding this method allows failed
     * conversions to provide a useful value. For example, if a string to value conversion failed, [nullValue] could
     * return a user-friendly string explaining that the conversion failed.
     */
    fun nullValue(): To? = null
}

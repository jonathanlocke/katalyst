package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.nucleus.language.errors.ErrorBehavior
import jonathanlocke.katalyst.nucleus.language.errors.behaviors.ReturnResult
import jonathanlocke.katalyst.nucleus.language.errors.behaviors.Throw
import kotlin.reflect.KClass

/**
 * A converter converts from a [From] type to a [To] type.
 *
 * **Conversions**
 *
 * - [convert] - Converts the given [From] value to a [To] value. If an error occurs, the [ErrorBehavior] is called,
 *               which determines whether a null value is returned ([ReturnNull]) or an exception is thrown ([Throw]).
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 *
 * @param From Source type
 * @param To Destination type
 * @see ErrorBehavior
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
     * Converts the given [From] value to a [To] value. If an error occurs, the [ErrorBehavior] is called and
     * that determines whether a null value is returned ([ReturnResult]) or an exception is thrown ([Throw]).
     * @param from The value to convert
     * @param errorBehavior The error handler to use when the conversion fails. Defaults to [Throw] if not provided.
     * @return Returns [To] if the conversion succeeded. If the conversion failed, returns null unless the error handler
     * throws an exception
     * @throws Exception Thrown by error handler if the conversion fails and the errorBehavior is [Throw]
     *
     * @see ErrorBehavior
     * @see ReturnResult
     * @see Throw
     */
    fun convert(from: From?, errorBehavior: ErrorBehavior<To?> = Throw()): To?

    /**
     * The value to use for conversions that result in a null [To] value. Overriding this method allows failed
     * conversions to provide a useful value. For example, if a string to value conversion failed, [nullValue] could
     * return a user-friendly string explaining that the conversion failed.
     */
    fun nullValue(): To? = null
}

package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.problems.listeners.Return
import jonathanlocke.katalyst.nucleus.language.problems.listeners.Throw
import kotlin.reflect.KClass

/**
 * Converts [From] -> [To]
 *
 * **Properties**
 *
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 *
 * **Methods**
 *
 * - [convert] - Converts the given [From] value to a [To] value. If an error occurs, the [ProblemListener] is called,
 *               which determines whether a null value is returned or an exception is thrown.
 *
 * @param From Source type
 * @param To Destination type
 *
 * @see Return
 * @see Throw
 * @see Problem
 * @see ProblemListener
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
     * Converts the given [From] value to a [To] value. If an error occurs, the [ProblemListener] is called and
     * that determines whether a null value is returned ([Return]) or an exception is thrown ([Throw]).
     * @param from The value to convert
     * @param listener The error handler to use when the conversion fails. Defaults to [Throw] if not provided.
     * @return Returns [To] if the conversion succeeded. If the conversion failed, returns null unless the error handler
     * throws an exception
     * @throws Exception Thrown by error handler if the conversion fails and the listener is [Throw]
     *
     * @see ProblemListener
     * @see Return
     * @see Throw
     */
    fun convert(from: From?, listener: ProblemListener = Throw()): To?

    /**
     * The value to use for conversions that result in a null [To] value. Overriding this method allows failed
     * conversions to provide a useful value. For example, if a string to value conversion failed, [nullValue] could
     * return a user-friendly string explaining that the conversion failed.
     */
    fun nullValue(): To? = null
}

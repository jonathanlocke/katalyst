package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter
import jonathanlocke.katalyst.nucleus.language.problems.reporters.ReturnNull
import jonathanlocke.katalyst.nucleus.language.problems.reporters.Throw
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
 * - [convert] - Converts the given [From] value to a [To] value. If an error occurs, the [ProblemReporter] is called,
 *               which determines whether a null value is returned or an exception is thrown.
 *
 * @param From Source type
 * @param To Destination type
 *
 * @see ReturnNull
 * @see Throw
 * @see Problem
 * @see ProblemReporter
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
     * Converts the given [From] value to a [To] value. If an error occurs, the [ProblemReporter] is called and
     * that determines whether a null value is returned ([ReturnNull]) or an exception is thrown ([Throw]).
     * @param from The value to convert
     * @param reporter The error handler to use when the conversion fails. Defaults to [Throw] if not provided.
     * @return Returns [To] if the conversion succeeded. If the conversion failed, returns null unless the error handler
     * throws an exception
     * @throws Exception Thrown by error handler if the conversion fails and the reporter is [Throw]
     *
     * @see ProblemReporter
     * @see ReturnNull
     * @see Throw
     */
    fun convert(from: From?, reporter: ProblemReporter<To> = Throw()): To?

    /**
     * The value to use for conversions that result in a null [To] value. Overriding this method allows failed
     * conversions to provide a useful value. For example, if a string to value conversion failed, [nullValue] could
     * return a user-friendly string explaining that the conversion failed.
     */
    fun nullValue(): To? = null
}

package jonathanlocke.katalyst.conversion.converters

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemException
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ReturnOnError
import jonathanlocke.katalyst.problems.handlers.ThrowOnError
import jonathanlocke.katalyst.problems.handlers.ThrowOnError.Companion.throwOnError
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.validation.ValidationResult
import jonathanlocke.katalyst.validation.ValidatorBase

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
 * - [convert] - Converts the given [From] value to a [To] value. If an error occurs, the [ProblemHandler] is called,
 *               which determines whether a null value is returned or an exception is thrown.
 *
 * @param From Source type
 * @param To Destination type
 *
 * @see ReturnOnError
 * @see ThrowOnError
 * @see Problem
 * @see ProblemHandler
 */
interface Converter<From : Any, To : Any> {

    /**
     * All converters are validators. If [convert] fails, it will report the conversion error
     * as a [Problem] to its [ProblemHandler]. Because [ValidationResult] is a [ProblemHandler],
     * simply passing the result object to [convert] will cause it to capture any errors that
     * occur during conversion in the result.
     */
    fun asValidator() = object : ValidatorBase<From>() {
        override fun onValidate(value: From, result: ValidationResult<From>) {
            convert(value, result)
        }
    }

    /**
     * The type of the [From] class
     */
    val from: ValueType<From>

    /**
     * The type of the [To] class
     */
    val to: ValueType<To>

    /**
     * Converts the given [From] value to a [To] value. If an error occurs, the [ProblemHandler] is called and
     * that determines whether a null value is returned ([ReturnOnError]) or an exception is thrown ([ThrowOnError]).
     * @param from The value to convert
     * @param problemHandler The error handler to use when the conversion fails. Defaults to [ThrowOnError] if not provided.
     * @return Returns [To] if the conversion succeeded. If the conversion failed, returns null unless the error handler
     * throws an exception
     * @throws ProblemException Thrown by error handler if the conversion fails and the handler is [ThrowOnError]
     *
     * @see ProblemHandler
     * @see ReturnOnError
     * @see ThrowOnError
     */
    fun convert(from: From?, problemHandler: ProblemHandler = throwOnError): To?

    /**
     * The value to use for conversions that result in a null [To] value. Overriding this method allows failed
     * conversions to provide a useful value. For example, if a string to value conversion failed, [nullValue] could
     * return a user-friendly string explaining that the conversion failed.
     */
    fun nullValue(): To? = null
}

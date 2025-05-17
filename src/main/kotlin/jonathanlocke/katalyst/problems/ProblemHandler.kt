package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.categories.*
import jonathanlocke.katalyst.problems.handlers.PrefixingProblemHandler
import jonathanlocke.katalyst.problems.handlers.ReturnOnError
import jonathanlocke.katalyst.problems.handlers.ThrowOnError
import java.util.function.Supplier

/**
 * An [ProblemHandler] allows code to be flexible in how it handles problems in different usage contexts.
 *
 * In one context, it might be desirable for a method to throw an exception, while in another context, it might
 * be desirable for the same method to return a null value for performance reasons. The problem handlers [ThrowOnError]
 * and [ReturnOnError] respectively handle these situations.
 *
 * **Integer.parseInt()**
 *
 * The classic example of this problem is Java's [Integer.parseInt] method, which throws a [NumberFormatException] if
 * its argument is not a valid integer. While this may be convenient in many cases, in some use contexts (like
 * parsing large amounts of dirty data) throwing an exception can have unacceptable performance.
 *
 * In the specific case of [String.toInt] (which is bound by Kotlin to Integer.parseInt), Kotlin has provided an
 * alternative method that returns a null value in [String.toIntOrNull]. But this leaves something to be desired
 * because the functionality of integer parsing had to be duplicated.
 *
 * [ProblemHandler] provides a way to avoid this kind of duplication by allowing the caller of a method to
 * specify how the error handling should work.
 *
 * For example, the [Integer.parseInt] problem could be solved using [ProblemHandler] like this:
 *
 * ```
 * fun String.parseInt(problemHandler: ProblemHandler = throwOnError): Int? { ... }
 * ```
 *
 * With this hypothetical implementation of [String.parseInt], if the caller wanted an exception thrown, they could
 * invoke the method like this:
 *
 * ```
 * "5.6".parseInt()
 * ```
 *
 * Since the default problem handler is [ThrowOnError], an exception will be thrown. But if the caller needed to avoid
 * throwing an exception, they could do this, which would cause the same method to return null instead:
 *
 * ```
 * dirtyText.parseInt(ReturnNull())
 * ```
 *
 * Note that because the return value of the extension method here is [Int]?, it can be helpful to provide an overload
 * that invokes the version that throws an exception and returns a *non-nullable* [Int] value:
 *
 * ```
 * fun String.parseInt(): Int = parseInt()
 * ```
 *
 * **Bytes**
 *
 * For another example of how [ProblemHandler] can be used effectively, see [jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.parseBytes]
 *
 * @see ThrowOnError
 * @see ReturnOnError
 * @see Problem
 * @see Error
 * @see Warning
 */
interface ProblemHandler {

    fun problems(): ProblemList

    fun prefixed(prefix: String) = PrefixingProblemHandler(prefix, this)

    fun handle(problem: Problem) {
        problems().add(problem)
    }

    fun handleFrom(problemSource: ProblemSource) {
        problemSource.handlers().add(this)
    }

    /**
     * Forces a failure state that throws an exception which includes all problems this handler has encountered
     */
    fun fail(message: String) {
        ProblemException.fail(message, problems())
    }

    fun <Value> requireOrFail(value: Value?, message: String = "Cannot be null") = requireOrFail(value != null, message)

    fun requireOrFail(condition: Boolean, message: String, code: Runnable? = null): Boolean {
        if (!condition) fail(message) else code?.run()
        return condition
    }

    fun requireOrError(condition: Boolean, message: String, code: Runnable? = null): Boolean {
        if (condition) error(message) else code?.run()
        return condition
    }

    fun failure(message: String, cause: Throwable? = null, value: Any? = null) = handle(Failure(message, cause, value))

    fun info(message: String, cause: Throwable? = null, value: Any? = null) = handle(Info(message))

    fun trace(message: String, cause: Throwable? = null, value: Any? = null) = handle(Trace(message))

    fun error(message: String, cause: Throwable? = null, value: Any? = null) = handle(Error(message, cause, value))

    fun warning(message: String, cause: Throwable? = null, value: Any? = null) = handle(Warning(message, cause, value))

    fun tryBoolean(message: String, code: Supplier<Boolean>): Boolean = try {
        code.get()
    } catch (e: Exception) {
        error(message, e)
        false
    }

    /**
     * Guards the given functional code block by catching exceptions and reporting them as errors
     */
    fun <Value> tryValue(message: String, code: Supplier<Value>): Value? = try {
        code.get()
    } catch (e: Exception) {
        error(message, e)
        null
    }
}
package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.categories.Error
import jonathanlocke.katalyst.problems.categories.Warning
import jonathanlocke.katalyst.validation.problems.ValidationError
import jonathanlocke.katalyst.validation.problems.ValidationWarning

/**
 * Base class for different kinds of problems, including:
 *
 *  - [Error]
 *  - [Warning]
 *  - [ValidationError]
 *  - [ValidationWarning]
 *
 * @param message A message describing the problem
 * @param cause Any exception that caused the problem
 * @param value Any value associated with the problem
 * @see Error
 * @see Warning
 * @see ValidationError
 * @see ValidationWarning
 */
abstract class Problem(val message: String, val cause: Throwable? = null, val value: Any? = null) {

    enum class Effect { STOP, CONTINUE }

    abstract val effect: Effect

    override fun toString(): String {
        val causeText = cause?.let { ", cause: $it" } ?: ""
        val valueText = value?.let { ", value: $it" } ?: ""
        return message + causeText + valueText
    }
}
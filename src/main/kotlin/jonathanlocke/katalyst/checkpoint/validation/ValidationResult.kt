package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationError
import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationWarning
import jonathanlocke.katalyst.nucleus.language.functional.Reporter
import jonathanlocke.katalyst.nucleus.language.problems.Error
import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.Warning
import jonathanlocke.katalyst.nucleus.values.Count.Companion.toCount

/**
 * A collection of validation problems.
 *
 * @property problems The list of validation problems
 * @property isValid True if there are no validation problems
 * @property isInvalid True if there are validation problems
 * @property count The number of validation problems
 *
 * @see Problem
 * @see Error
 * @see Warning
 */
class ValidationResult<Value : Any>(val value: Value) {

    val problems = mutableListOf<Problem>()
    val isValid = problems.none { it is Error }
    val isInvalid = problems.isNotEmpty()
    val count = problems.size.toCount()

    fun validationError(message: String, cause: Throwable? = null) =
        problems.add(ValidationError(message, cause, value))

    fun validationWarning(message: String, cause: Throwable? = null) =
        problems.add(ValidationWarning(message, cause, value))

    fun reporter(): Reporter<Value> = object : Reporter<Value> {
        override fun report(problem: Problem): Value? {
            if (problem is Error) validationError(problem.message, problem.cause)
            if (problem is Warning) validationWarning(problem.message, problem.cause)
            return null
        }
    }
}

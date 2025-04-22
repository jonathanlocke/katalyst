package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationError
import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationWarning
import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListenerBase
import jonathanlocke.katalyst.nucleus.language.problems.categories.Error
import jonathanlocke.katalyst.nucleus.language.problems.categories.Warning
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.toCount

/**
 * A collection of validation problems.
 *
 * **Methods**
 *
 * - [validationError] - Reports a validation error
 * - [validationWarning] - Reports a validation warning
 * - [listener] - Returns a [ProblemListener] that can be used to report validation problems
 *
 * @param Value The type of the value to validate
 * @param value The value to validate
 *
 * @see ProblemListener
 * @see ValidationError
 * @see ValidationWarning
 * @see Problem]
 *
 * @property problems The list of validation problems
 * @property isValid True if there are no validation problems
 * @property isInvalid True if there are validation problems
 * @property problemCount The total number of validation problems
 *
 * @see Problem
 * @see Error
 * @see Warning
 */
class ValidationResult<Value : Any>(val value: Value) {

    val problems = mutableListOf<Problem>()
    val isValid = problems.none { it is Error }
    val isInvalid = problems.isNotEmpty()
    val problemCount = problems.size.toCount()

    fun validationError(message: String, cause: Throwable? = null) =
        problems.add(ValidationError(message, cause, value))

    fun validationWarning(message: String, cause: Throwable? = null) =
        problems.add(ValidationWarning(message, cause, value))

    fun listener(): ProblemListener = object : ProblemListenerBase() {
        override fun onProblem(problem: Problem) {
            if (problem is Error) validationError(problem.message, problem.cause)
            if (problem is Warning) validationWarning(problem.message, problem.cause)
        }
    }
}

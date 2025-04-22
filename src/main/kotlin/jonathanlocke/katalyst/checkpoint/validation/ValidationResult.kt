package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationError
import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationWarning
import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemList
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.problems.categories.Error
import jonathanlocke.katalyst.nucleus.language.problems.categories.Warning

/**
 * A collection of validation problems.
 *
 * **Methods**
 *
 * - [validationError] - Reports a validation error
 * - [validationWarning] - Reports a validation warning
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
 *
 * @see Problem
 * @see ValidationError
 * @see ValidationWarning
 */
class ValidationResult<Value : Any>(val value: Value) : ProblemListener {

    override val problems = ProblemList()

    val isValid = problems.isValid
    val isInvalid = problems.isInvalid
    val errors = problems.errors
    val warnings = problems.warnings

    override fun problem(problem: Problem) {
        if (problem is Error) validationError(problem.message, problem.cause)
        if (problem is Warning) validationWarning(problem.message, problem.cause)
    }

    fun validationError(message: String, cause: Throwable? = null) =
        problems.add(ValidationError(message, cause, value))

    fun validationWarning(message: String, cause: Throwable? = null) =
        problems.add(ValidationWarning(message, cause, value))
}

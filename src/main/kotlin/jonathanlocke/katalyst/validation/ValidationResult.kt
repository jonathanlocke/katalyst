package jonathanlocke.katalyst.validation

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemList
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.categories.Error
import jonathanlocke.katalyst.problems.categories.Warning
import jonathanlocke.katalyst.validation.problems.ValidationError
import jonathanlocke.katalyst.validation.problems.ValidationWarning

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

    val isValid = problems.isValid()
    val isInvalid = problems.isInvalid()
    val errors = problems.errors()
    val warnings = problems.warnings()

    override fun receive(problem: Problem) {
        if (problem is Error) validationError(problem.message, problem.cause)
        if (problem is Warning) validationWarning(problem.message, problem.cause)
    }

    fun validationError(message: String, cause: Throwable? = null) =
        problems.add(ValidationError(message, cause, value))

    fun validationWarning(message: String, cause: Throwable? = null) =
        problems.add(ValidationWarning(message, cause, value))
}

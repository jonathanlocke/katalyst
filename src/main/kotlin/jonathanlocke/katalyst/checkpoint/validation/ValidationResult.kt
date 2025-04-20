package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationError
import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationWarning
import jonathanlocke.katalyst.nucleus.values.Count.Companion.toCount

/**
 * A collection of validation problems.
 *
 * @param Value The type of the value to validate
 * @property problems The list of validation problems
 * @property isValid True if there are no validation problems
 * @property isInvalid True if there are validation problems
 * @property count The number of validation problems
 *
 * @see ValidationProblem
 * @see ValidationError
 */
class ValidationResult<Value>(val value: Value) {

    val problems = mutableListOf<ValidationProblem<Value>>()
    val isValid = problems.none { it is ValidationError }
    val isInvalid = problems.isNotEmpty()
    val count = problems.size.toCount()

    fun error(message: String, value: Value) = problems.add(ValidationError(message, value))
    fun warning(message: String, value: Value) = problems.add(ValidationWarning(message, value))
}

package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.nucleus.language.errors.Problem
import jonathanlocke.katalyst.nucleus.language.errors.problems.Error
import jonathanlocke.katalyst.nucleus.language.errors.problems.Warning
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
class ValidationResult<Value>(val value: Value) {

    val problems = mutableListOf<Problem>()
    val isValid = problems.none { it is Error }
    val isInvalid = problems.isNotEmpty()
    val count = problems.size.toCount()

    fun error(message: String) = problems.add(Error(message, value = value))
    fun warning(message: String) = problems.add(Warning(message, value = value))
}

package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.nucleus.values.Count.Companion.toCount

class ValidationErrors<T> {

    val errors = listOf<ValidationError<T>>()
    val isValid = errors.isEmpty()
    val isInvalid = errors.isNotEmpty()
    val count = errors.size.toCount()
}

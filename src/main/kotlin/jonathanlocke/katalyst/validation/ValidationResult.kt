package jonathanlocke.katalyst.validation

import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusList
import jonathanlocke.katalyst.status.categories.Error
import jonathanlocke.katalyst.status.categories.Warning
import jonathanlocke.katalyst.validation.status.ValidationError
import jonathanlocke.katalyst.validation.status.ValidationWarning

/**
 * A collection of validation statuses.
 *
 * **Methods**
 *
 * - [validationError] - Reports a validation error
 * - [validationWarning] - Reports a validation warning
 *
 * @param Value The type of the value to validate
 * @param value The value to validate
 *
 * @see StatusHandler
 * @see ValidationError
 * @see ValidationWarning
 * @see Status]
 *
 * @property statuses The list of validation statuses
 *
 * @see Status
 * @see ValidationError
 * @see ValidationWarning
 */
class ValidationResult<Value : Any>(val value: Value) : StatusHandler {

    override fun statuses() = StatusList()

    val isValid = statuses().isValid()
    val isInvalid = statuses().isInvalid()
    val errors = statuses().errors()
    val warnings = statuses().warnings()

    override fun handle(status: Status): Boolean {
        if (status is Error) validationError(status.message, status.cause)
        if (status is Warning) validationWarning(status.message, status.cause)
        return true
    }

    fun validationError(message: String, cause: Throwable? = null) =
        statuses().add(ValidationError(message, cause, value))

    fun validationWarning(message: String, cause: Throwable? = null) =
        statuses().add(ValidationWarning(message, cause, value))
}

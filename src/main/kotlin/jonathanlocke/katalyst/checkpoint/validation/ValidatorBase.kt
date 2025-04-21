package jonathanlocke.katalyst.checkpoint.validation

/**
 * A base class for implementing a [Validator].
 *
 *  - [onValidate] - Called when [validate] is called
 *  - [result] - A collection of validation problems
 *  - [isValid] - True if there are no validation problems
 *  - [isInvalid] - True if there are validation problems
 *
 * @param Value The type of value to validate
 * @see Validator
 * @see ValidationResult
 */
abstract class ValidatorBase<Value : Any> : Validator<Value> {

    val isValid = result.isValid
    val isInvalid = result.isInvalid

    protected lateinit var result: ValidationResult<Value>

    /**
     * Performs validation of the given value, calling the error handler if it is not valid.
     * @param value The value to validate
     * @return The result of validation
     */
    final override fun validate(value: Value): ValidationResult<Value> {

        // Create the validation result for the given value,
        this.result = ValidationResult(value)

        try {

            // call the subclass to validate the value
            onValidate(value, result)

        } catch (e: Exception) {

            // and if an exception is thrown, record an error.
            error("Unexpected exception: ${e.message}")
        }

        return result
    }

    fun validationError(message: String) = result.validationError(message)
    fun validationWarning(message: String) = result.validationWarning(message)

    protected abstract fun onValidate(value: Value, result: ValidationResult<Value>)
}
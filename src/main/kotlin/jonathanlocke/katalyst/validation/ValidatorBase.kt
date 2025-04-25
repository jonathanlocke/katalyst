package jonathanlocke.katalyst.validation

/**
 * A base class for implementing a [Validator].
 *
 *  **Methods**
 *
 *  - [validate] - Validates the given value and returns a [ValidationResult]
 *
 *  **Extension Points**
 *
 *  - [onValidate] - Called when [validate] is called
 *
 *  **Protected**
 *
 *  - [validationError] - Records an error to the [ValidationResult]
 *  - [validationWarning] - Records a warning to the [ValidationResult]
 *
 * @param Value The type of value to validate
 * @see Validator
 * @see ValidationResult
 */
abstract class ValidatorBase<Value : Any> : Validator<Value> {

    /**
     * The validation result for the calling thread
     */
    private val result = ThreadLocal<ValidationResult<Value>>()

    /**
     * Performs validation of the given value, calling the error handler if it is not valid.
     * @param value The value to validate
     * @return The result of validation
     */
    final override fun validate(value: Value): ValidationResult<Value> {

        // Create the validation result for the given value,
        this.result.set(ValidationResult(value))

        try {

            // call the subclass to validate the value
            onValidate(value, result.get())

        } catch (e: Exception) {

            // and if an exception is thrown, record an error.
            validationError("Unexpected exception: ${e.message}")
        }

        return result.get()
    }

    /**
     * Records an error to the [ValidationResult]
     */
    protected fun validationError(message: String, cause: Throwable? = null) =
        result.get().validationError(message, cause)

    /**
     * Records a warning to the [ValidationResult]
     */
    protected fun validationWarning(message: String, cause: Throwable? = null) =
        result.get().validationWarning(message, cause)

    /**
     * Extension point for subclasses to validate the given value.
     *
     * @param value The value to validate
     * @param result The [ValidationResult] to record errors and warnings to
     * @throws Exception If an exception is thrown, it will be caught and recorded as an error
     */
    protected abstract fun onValidate(value: Value, result: ValidationResult<Value>)
}
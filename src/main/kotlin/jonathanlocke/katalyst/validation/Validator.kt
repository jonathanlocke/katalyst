package jonathanlocke.katalyst.validation

import jonathanlocke.katalyst.validation.Validator.Companion.isValid
import jonathanlocke.katalyst.validation.Validator.Companion.requireValid
import jonathanlocke.katalyst.validation.Validator.Companion.validate
import jonathanlocke.katalyst.validation.Validator.Companion.validator

/**
 * A validator checks values and returns a [ValidationResult].
 *
 * Validators can be implemented by subclassing [ValidatorBase] or by passing a lambda function to [validator].
 *
 * **Extension Points**
 *
 * o [validate] - Validates the given value, returning a [ValidationResult]]
 *
 * **Companions**
 *
 * - [isValid] - Checks if a value is valid by calling [validate] on each validator in the list
 * - [requireValid] - Checks if a value is valid and throws an exception if it is not
 * - [validator] - Creates a validator from a lambda function
 *
 * @see ValidatorBase
 *
 * @param Value The type of value to validate
 */
interface Validator<Value : Any> {

    /**
     * Performs validation of the given value
     *
     * @param value The value to validate
     * @return Returns the result of validation.
     */
    fun validate(value: Value): ValidationResult<Value>

    companion object {

        /**
         * Checks if this value is valid by calling [validate] on each validator in the list.
         * @param validators The list of validators to check
         * @return Returns true if all validators validate successfully. If a validator encounters a status,
         *         the [ErrorHandler] is called, which determines whether a null value is returned ([ReturnNull]) or
         *         an exception is thrown ([ThrowOnError]).
         */
        fun <Value : Any> isValid(value: Value, vararg validators: Validator<Value>): Boolean =
            validators.all { it.validate(value).isValid }

        /**
         * Checks if this value is valid by calling [validate] on each validator in the list.
         * @param validator The validator to check
         * @return Returns this value for method chaining.
         */
        fun <Value : Any> requireValid(value: Value, vararg validators: Validator<Value>): Value {
            validators.all { it.validate(value).isValid }
            return value
        }

        /**
         * Calls each validator on the given value merging the validation result of each validator into a composite
         *
         * @param value The value to validate
         * @param validators The list of validators to check
         * @return The merged validation result
         */
        fun <Value : Any> validate(
            value: Value,
            validators: List<Validator<Value>>,
        ): ValidationResult<Value> {
            val result = ValidationResult(value)
            validators.stream()
                .map { it -> it.validate(value) }
                .forEach { it -> it.statuses().forEach(result::handle) }
            return result
        }

        /**
         * Calls each validator on the given value merging the validation result of each validator into a composite
         *
         * @param value The value to validate
         * @param validators The list of validators to check
         * @return The merged validation result
         */
        @SafeVarargs
        fun <Value : Any> validate(
            value: Value,
            vararg validators: Validator<Value>,
        ): ValidationResult<Value> {
            return validate(value, validators.toList())
        }

        /**
         * Creates a validator from a lambda function. The purpose of this function is purely to make it easier and
         * more concise to create validators.
         * @param lambda The lambda function to use for validation
         * @return Returns a validator that calls the given lambda function to validate values.
         */
        fun <Value : Any> validator(lambda: (value: Value, result: ValidationResult<Value>) -> Unit):
                Validator<Value> = object : ValidatorBase<Value>() {
            override fun onValidate(value: Value, result: ValidationResult<Value>) {
                lambda.invoke(value, result)
            }
        }
    }
}

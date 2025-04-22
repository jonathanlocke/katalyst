package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.Validator.Companion.ensureValid
import jonathanlocke.katalyst.checkpoint.validation.Validator.Companion.isValid
import jonathanlocke.katalyst.checkpoint.validation.Validator.Companion.validator

/**
 * A validator checks values and returns a [ValidationResult].
 *
 * Validators can be implemented by subclassing [ValidatorBase] or by passing a lambda function to [validator].
 *
 * **Usage**
 *
 * - [isValid] - Checks if a value is valid by calling [validate] on each validator in the list
 * - [ensureValid] - Checks if a value is valid and throws an exception if it is not
 * - [validator] - Creates a validator from a lambda function
 *
 * **Examples**
 *
 * - [isValid] - Checks if a value is valid by calling one or more validators
 * - [ensureValid] - Ensures that a validator is valid or an exception is thrown
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
         * @return Returns true if all validators validate successfully. If a validator encounters a problem,
         *         the [ErrorHandler] is called, which determines whether a null value is returned ([ReturnNull]) or
         *         an exception is thrown ([Throw]).
         */
        fun <Value : Any> Value.isValid(vararg validators: Validator<Value>): Boolean =
            validators.all { it.validate(this).isValid }

        /**
         * Checks if this value is valid by calling [validate] on each validator in the list.
         * @param validator The validator to check
         * @return Returns this value for method chaining.
         */
        fun <Value : Any> Value.ensureValid(vararg validators: Validator<Value>): Value {
            validators.all { it.validate(this).isValid() }
            return this
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

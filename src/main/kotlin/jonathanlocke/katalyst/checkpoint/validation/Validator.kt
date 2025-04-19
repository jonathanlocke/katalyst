package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.handlers.Throw

interface Validator<T> {

    /**
     * Performs validation of the given value, calling the error handler if it is not valid.
     *
     * @return Returns true if validation succeeded. If validation fails, returns false unless the error handler
     * throws an exception
     * @throws Exception Thrown by error handler if the implementation is [Throw]
     */
    fun validate(value: T, errorHandler: ErrorHandler<Boolean>): Boolean

    companion object {

        fun <T : Any> T.isValid(vararg validators: Validator<T>): Boolean =
            validators.all { it.validate(this, Throw()) }

        fun <T : Any> T.validated(validator: Validator<T>): T {
            validator.validate(this, Throw())
            return this
        }

        fun <T> validator(lambda: (value: T, errorHandler: ErrorHandler<Boolean>) -> Boolean): Validator<T> =
            object : Validator<T> {
                override fun validate(value: T, errorHandler: ErrorHandler<Boolean>): Boolean =
                    lambda(value, errorHandler)
            }
    }
}

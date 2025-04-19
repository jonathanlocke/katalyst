package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

abstract class ValidatorBase<T> : Validator<T> {

    val errors = ValidationErrors<T>()
    val isValid = errors.isValid
    val isInvalid = errors.isInvalid

    override fun validate(value: T, errorHandler: ErrorHandler<Boolean>): Boolean {
        onValidate()
        return errors.isValid
    }

    protected abstract fun onValidate()
}
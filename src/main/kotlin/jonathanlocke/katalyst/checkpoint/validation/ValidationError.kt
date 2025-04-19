package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.ValidationError.Type.ERROR
import jonathanlocke.katalyst.checkpoint.validation.ValidationError.Type.WARNING

class ValidationError<T>(val message: String, val type: Type, val value: T) {

    enum class Type { ERROR, WARNING }

    companion object {
        fun <T> error(message: String, value: T) = ValidationError(message, ERROR, value)
        fun <T> warning(message: String, value: T) = ValidationError(message, WARNING, value)
    }
}
package jonathanlocke.katalyst.checkpoint.validation.values

import jonathanlocke.katalyst.checkpoint.validation.Validator.Companion.validator
import jonathanlocke.katalyst.checkpoint.validation.ValidatorBase
import java.util.function.Consumer

class Numbers {
    companion object {

        fun isLessThan(max: Number) = object : ValidatorBase<Number>() {
            override fun onValidate(value: Number) {
                if (value.toDouble() > max.toDouble()) {
                    error("$value must be less than $max")
                }
            }
        }

        fun isGreaterThan(max: Number) = validator { value: Number, onError: Consumer<String> ->
            value.toDouble() > max.toDouble() || onError.accept("$value must be greater than $max")
        }

        fun isLessThanOrEqualTo(max: Number) = validator { value: Number, errorHandler ->
            value.toDouble() <= max.toDouble() || errorHandler.error("$value must be less than $max") ?: false
        }

        fun isGreaterThanOrEqualTo(max: Number) = validator { value: Number, errorHandler ->
            value.toDouble() >= max.toDouble() || errorHandler.error("$value must be greater than $max") ?: false
        }

        fun isEqualTo(max: Number) = validator { value: Number, errorHandler ->
            value.toDouble() == max.toDouble() || errorHandler.error("$value must be greater than $max") ?: false
        }

        fun isNotEqualTo(max: Number) = validator { value: Number, errorHandler ->
            value.toDouble() != max.toDouble() || errorHandler.error("$value must be greater than $max") ?: false
        }
    }
}

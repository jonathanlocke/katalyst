package jonathanlocke.katalyst.checkpoint.validation.values

import jonathanlocke.katalyst.checkpoint.validation.Validator.Companion.validator

class Numbers {
    companion object {

        fun isLessThan(max: Number) = validator { value: Number, errorHandler ->
            value.toDouble() < max.toDouble() || errorHandler.error("$value must be less than $max") ?: false
        }

        fun isGreaterThan(max: Number) = validator { value: Number, errorHandler ->
            value.toDouble() > max.toDouble() || errorHandler.error("$value must be greater than $max") ?: false
        }
    }
}

package jonathanlocke.katalyst.validation.values

import jonathanlocke.katalyst.validation.Validator
import jonathanlocke.katalyst.validation.Validator.Companion.validator

/**
 * Validators for numbers.
 *
 * @see Validator
 */
object NumberValidators {

    fun isLessThan(max: Number) = validator { value: Number, result ->
        value.toDouble() < max.toDouble() || result.validationError("$value must be greater than $max")
    }

    fun isGreaterThan(max: Number) = validator { value: Number, result ->
        value.toDouble() > max.toDouble() || result.validationError("$value must be greater than $max")
    }

    fun isLessThanOrEqualTo(max: Number) = validator { value: Number, result ->
        value.toDouble() <= max.toDouble() || result.validationError("$value must be less than $max")
    }

    fun isGreaterThanOrEqualTo(max: Number) = validator { value: Number, result ->
        value.toDouble() >= max.toDouble() || result.validationError("$value must be greater than $max")
    }

    fun isEqualTo(max: Number) = validator { value: Number, result ->
        value.toDouble() == max.toDouble() || result.validationError("$value must be greater than $max")
    }

    fun isNotEqualTo(max: Number) = validator { value: Number, result ->
        value.toDouble() != max.toDouble() || result.validationError("$value must be greater than $max")
    }
}


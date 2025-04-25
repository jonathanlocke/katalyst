package jonathanlocke.katalyst.validation

import jonathanlocke.katalyst.validation.Validator.Companion.isValid
import jonathanlocke.katalyst.validation.values.NumberValidators.Companion.isGreaterThan
import jonathanlocke.katalyst.validation.values.NumberValidators.Companion.isLessThan
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidationTest {

    @Test
    fun testSuccess() {
        assertTrue(isValid(1, isGreaterThan(0), isLessThan(2)))
    }
}

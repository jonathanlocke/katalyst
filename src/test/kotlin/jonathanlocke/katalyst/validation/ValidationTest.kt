package jonathanlocke.katalyst.validation

import jonathanlocke.katalyst.validation.Validator.Companion.isValid
import jonathanlocke.katalyst.validation.values.NumberValidators.isGreaterThan
import jonathanlocke.katalyst.validation.values.NumberValidators.isLessThan
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidationTest {

    @Test
    fun testSuccess() {
        assertTrue(isValid(1, isGreaterThan(0), isLessThan(2)))
    }
}

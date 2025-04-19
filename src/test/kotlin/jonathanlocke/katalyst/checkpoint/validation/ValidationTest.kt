package jonathanlocke.katalyst.checkpoint.validation

import jonathanlocke.katalyst.checkpoint.validation.Validator.Companion.isValid
import jonathanlocke.katalyst.checkpoint.validation.values.Numbers.Companion.isGreaterThan
import jonathanlocke.katalyst.checkpoint.validation.values.Numbers.Companion.isLessThan
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidationTest {

    @Test
    fun testSuccess() {
        assertTrue(1.isValid(isGreaterThan(0), isLessThan(2)))
    }
}

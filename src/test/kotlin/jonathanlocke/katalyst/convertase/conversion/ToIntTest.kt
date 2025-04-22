package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.toValue
import jonathanlocke.katalyst.convertase.conversion.strings.values.StringToNumber.Companion.intConverter
import jonathanlocke.katalyst.nucleus.language.problems.listeners.ReturnNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ToIntTest {

    @Test
    fun testSuccess() {
        assert(10 == "10".toValue(intConverter))
        assert(1_000 == "1000".toValue(intConverter))
    }

    @Test
    fun testReturnNull() {
        assertThrows<Exception> { "xyz".toValue(intConverter) }
        assertThrows<Exception> { "1.0".toValue(intConverter) }
        assertNull("xyz".toValue(intConverter, ReturnNull()))
        assertNull("1.0".toValue(intConverter, ReturnNull()))
    }
}

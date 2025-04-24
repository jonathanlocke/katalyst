package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter.Companion.convert
import jonathanlocke.katalyst.convertase.conversion.converters.strings.values.StringToNumber.Companion.intConverter
import jonathanlocke.katalyst.nucleus.problems.listeners.Return
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ToIntTest {

    @Test
    fun testSuccess() {
        assert(10 == "10".convert(intConverter))
        assert(1_000 == "1000".convert(intConverter))
    }

    @Test
    fun testReturnNull() {
        assertThrows<Exception> { "xyz".convert(intConverter) }
        assertThrows<Exception> { "1.0".convert(intConverter) }
        assertNull("xyz".convert(intConverter, Return()))
        assertNull("1.0".convert(intConverter, Return()))
    }
}

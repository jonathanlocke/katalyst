package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.conversion.converters.strings.StringConversions.convert
import jonathanlocke.katalyst.conversion.converters.strings.values.StringToNumber.intConverter
import jonathanlocke.katalyst.status.StatusHandlers.returnOnError
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
        assertNull("xyz".convert(intConverter, returnOnError))
        assertNull("1.0".convert(intConverter, returnOnError))
    }
}

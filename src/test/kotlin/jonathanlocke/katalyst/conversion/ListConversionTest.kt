package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.convertToList
import jonathanlocke.katalyst.conversion.converters.strings.values.StringToNumber.Companion.intConverter
import jonathanlocke.katalyst.problems.listeners.Return
import jonathanlocke.katalyst.text.parsing.Separator.Companion.COLON_SEPARATOR
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ListConversionTest {

    @Test
    fun testSuccess() {
        assert("1,2,3,4,5".convertToList(intConverter) == listOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testSeparators() {
        assert("1:2:3:4:5".convertToList(intConverter, COLON_SEPARATOR) == listOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testThrow() {
        assertThrows<Exception> { "1,2,x,4,5".convertToList(intConverter) }
    }

    @Test
    fun testReturnNull() {
        assertNull("1,2,x,4,5".convertToList(intConverter, listener = Return()))
    }
}

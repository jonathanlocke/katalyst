package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter.Companion.toList
import jonathanlocke.katalyst.convertase.conversion.converters.strings.values.StringToNumber.Companion.intConverter
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator.Companion.COLON_SEPARATOR
import jonathanlocke.katalyst.nucleus.problems.listeners.Return
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ListConversionTest {

    @Test
    fun testSuccess() {
        assert("1,2,3,4,5".toList(intConverter) == listOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testSeparators() {
        assert("1:2:3:4:5".toList(intConverter, COLON_SEPARATOR) == listOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testThrow() {
        assertThrows<Exception> { "1,2,x,4,5".toList(intConverter) }
    }

    @Test
    fun testReturnNull() {
        assertNull("1,2,x,4,5".toList(intConverter, listener = Return()))
    }
}

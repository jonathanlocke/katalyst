package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.conversion.converters.strings.StringConversions.Companion.convertToList
import jonathanlocke.katalyst.conversion.converters.strings.values.StringToNumber.Companion.intConverter
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.returnOnError
import jonathanlocke.katalyst.text.parsing.Separator.Companion.colonSeparator
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
        assert("1:2:3:4:5".convertToList(intConverter, colonSeparator) == listOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testThrow() {
        assertThrows<Exception> { "1,2,x,4,5".convertToList(intConverter) }
    }

    @Test
    fun testReturnNull() {
        assertNull("1,2,x,4,5".convertToList(intConverter, problemHandler = returnOnError))
    }
}

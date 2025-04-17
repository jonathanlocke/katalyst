package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.convertToList
import jonathanlocke.katalyst.convertase.strings.values.Primitives.Companion.ToInt
import jonathanlocke.katalyst.nucleus.language.errors.handlers.ReturnNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ListConversionTest {

    @Test
    fun testSuccess() {
        assert("1,2,3,4,5".convertToList(ToInt) == listOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testThrow() {
        assertThrows<Exception> { "1,2,x,4,5".convertToList(ToInt) }
    }

    @Test
    fun testReturnNull() {
        assertNull("1,2,x,4,5".convertToList(ToInt, errorHandler = ReturnNull()))
    }
}

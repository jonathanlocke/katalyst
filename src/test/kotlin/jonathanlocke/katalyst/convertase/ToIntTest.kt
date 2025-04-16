package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.convert
import jonathanlocke.katalyst.convertase.strings.values.ToInt
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ToIntTest {

    @Test
    fun test() {
        assert(1 == "1.0".convert(ToInt()))
        assertThrows<Exception> { "xyz".convert(ToInt()) }
        assert(1_000 == "1,000".convert(ToInt()))
    }
}

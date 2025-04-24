package jonathanlocke.katalyst.data.values

import jonathanlocke.katalyst.data.values.percent.Percent.Companion.DecimalFormat
import jonathanlocke.katalyst.data.values.percent.Percent.Companion.parsePercent
import jonathanlocke.katalyst.data.values.percent.Percent.Companion.percent
import jonathanlocke.katalyst.data.values.percent.Percent.Companion.percentConverter
import jonathanlocke.katalyst.problems.listeners.Return
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PercentTest {

    @Test
    fun testCreation() {
        assertEquals(percent(50).percent, 50.0)
        assertEquals(percent(75), percent(75))
    }

    @Test
    fun testFormatting() {
        assertEquals(percent(50).toString(), "50%")
        assertEquals(percent(75.5).format(DecimalFormat), "75.5%")
    }

    @Test
    fun testOperators() {
        assertEquals(percent(20) + percent(30), percent(50))
        assertEquals(percent(50) - percent(20), percent(30))
        assertEquals(percent(50) * 0.5, percent(25))
        assertEquals(percent(50) / 2.0, percent(25))
        assertEquals(percent(150).inRange(percent(0), percent(100)), percent(100))
    }

    @Test
    fun testConversions() {
        assertEquals(parsePercent("50%"), percent(50))
        assertEquals(parsePercent("75.5%"), percent(75.5))
        assertNull(parsePercent("invalid", Return()))
        assertEquals(percent(50).percent, 50.0)
        assertEquals(percent(50).toString(), "50%")
        assertEquals(percent(20) + percent(30), percent(50))
        assertEquals(percentConverter().convert("50%"), percent(50))
        assertEquals(percentConverter().convert("75.5%"), percent(75.5))
        assertNull(percentConverter().convert("invalid", Return()))
        assertEquals(percent(50).asUnitValue(), 0.5)
        assertEquals(percent(150).asUnitValue(), 1.5)
        assertEquals(percent(150).asZeroToOne(), 1.0)
    }

    @Test
    fun testComparisons() {
        assertEquals(percent(25).compareTo(percent(50)), -1)
        assertTrue(percent(0).isZero())
        assertFalse(percent(50).isZero())
        assertEquals(percent(25).max(percent(75)), percent(75))
        assertEquals(percent(25).min(percent(75)), percent(25))
    }
}
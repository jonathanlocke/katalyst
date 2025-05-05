package jonathanlocke.katalyst.data.values

import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.ThousandsSeparatedFormatter
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.countConverter
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.parseCount
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.toCount
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.returnOnError
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CountTest {

    @Test
    fun testCreation() {
        assertEquals(1.toCount().asLong(), 1)
        assertEquals(count(5), 5.toCount())
        assertThrows<Exception> { count(-1) }
        assertNull(count(-1, returnOnError))
    }

    @Test
    fun testFormatting() {
        assertEquals(5000.toCount().format(ThousandsSeparatedFormatter), "5,000")
        assertEquals(4000.toCount().toString(), "4000")
    }

    @Test
    fun testOperators() {
        assertEquals(count(1) + count(2), count(3))
        assertEquals(count(2) - count(1), count(1))
        assertEquals(count(2) * count(3), count(6))
        assertEquals(count(6) / count(2), count(3))
        assertEquals(count(7) % count(3), count(1))
        var count = count(1)
        count++
        assertEquals(count, count(2))
        count--
        assertEquals(count, count(1))
        assertThrows<Exception> { count(-1) }
        assertEquals(count(15).inRange(count(1), count(10)), 10.toCount())
    }

    @Test
    fun testConversions() {
        assertEquals(parseCount("5000"), 5000.toCount())
        assertEquals(parseCount("4,000"), 4000.toCount())
        assertNull(parseCount("monkey", returnOnError))
        assertEquals(count(1).asLong(), 1)
        assertEquals(count(1).asDouble(), 1.0)
        assertEquals(count(1).asFloat(), 1.0F)
        assertEquals(count(1).asInt(), 1)
        assertEquals(count(1).asShort(), 1.toShort())
        assertEquals(count(1).asByte(), 1.toByte())
        assertEquals(count(1).toString(), "1")
        assertEquals(count(1) + count(2), count(3))
        assertEquals(countConverter().convert("1"), count(1))

    }

    @Test
    fun testComparisons() {
        assertEquals(count(1).compareTo(count(2)), -1)
        assertTrue(count(0).isZero())
        assertFalse(count(5).isZero())
        assertEquals(count(1).max(count(3)), count(3))
        assertEquals(count(1).min(count(3)), count(1))
    }

    @Test
    fun testFunctions() {
        var x = 0
        count(10).loop { x++ }
        assertEquals(x, 10)
    }
}
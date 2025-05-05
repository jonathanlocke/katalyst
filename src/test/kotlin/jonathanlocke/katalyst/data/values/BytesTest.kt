package jonathanlocke.katalyst.data.values

import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.IecUnitsFormatter
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.SiUnitsFormatter
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.bytesConverter
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.parseBytes
import jonathanlocke.katalyst.data.values.numeric.bytes.Bytes.Companion.toBytes
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers.Companion.returnOnError
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BytesTest {

    @Test
    fun testCreation() {
        assertEquals(1.toBytes().asBytes(), 1.0)
        assertEquals(bytes(5), 5.toBytes())
        assertThrows<Exception> { bytes(-1) }
    }

    @Test
    fun testFormatting() {
        assertEquals(5000.toBytes().format(SiUnitsFormatter), "5KB")
        assertEquals(100.toBytes().toString(), "100 bytes")
        assertEquals(5000.toBytes().format(IecUnitsFormatter), "4.9KiB")
        assertEquals(100.toBytes().toString(), "100 bytes")
    }

    @Test
    fun testOperators() {
        assertEquals(bytes(1) + bytes(2), bytes(3))
        assertEquals(bytes(2) - bytes(1), bytes(1))
        assertEquals(bytes(2) * bytes(3), bytes(6))
        assertEquals(bytes(6) / 2, bytes(3))
        assertEquals(bytes(7) % 3, bytes(1))
        var bytes = bytes(1)
        bytes++
        assertEquals(bytes, bytes(2))
        bytes--
        assertEquals(bytes, bytes(1))
        assertThrows<Exception> { bytes(-1) }
        assertEquals(bytes(15).inRange(bytes(1), bytes(10)), 10.toBytes())
    }

    @Test
    fun testConversions() {
        assertEquals(parseBytes("5000"), 5000.toBytes())
        assertEquals(parseBytes("4K"), 4000.toBytes())
        assertNull(parseBytes("monkey", problemHandler = returnOnError))
        assertEquals(bytes(1).toString(), "1 byte")
        assertEquals(bytes(2).toString(), "2 bytes")
        assertEquals(bytes(1) + bytes(2), bytes(3))
        assertEquals(bytesConverter().convert("1"), bytes(1))
    }

    @Test
    fun testComparisons() {
        assertEquals(bytes(1).compareTo(bytes(2)), -1)
        assertTrue(bytes(0).isZero())
        assertFalse(bytes(5).isZero())
        assertEquals(bytes(1).max(bytes(3)), bytes(3))
        assertEquals(bytes(1).min(bytes(3)), bytes(1))
    }
}
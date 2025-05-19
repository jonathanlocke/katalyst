package jonathanlocke.katalyst.data.structures.maps

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeMap
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.status.StatusException
import jonathanlocke.katalyst.status.StatusList
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SafeMapTest {

    @Test
    fun testWarning() {
        val statuses = StatusList()
        val map = safeMap<Int, String>(warningSize = count(10), statusHandler = statuses)
        (1..100).forEach { map[it] = it.toString() }
        assert(statuses.size == 1)
        assert(statuses.isValid())
        assert(statuses.warnings() == 1)
        assert(statuses.errors() == 0)
    }

    @Test
    fun testError() {
        assertThrows<StatusException> {
            val map = safeMap<Int, String>(maximumSize = count(10))
            (1..100).forEach { map[it] = it.toString() }
        }
    }
}
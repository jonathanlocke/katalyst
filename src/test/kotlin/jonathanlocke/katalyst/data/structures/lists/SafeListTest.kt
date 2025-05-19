package jonathanlocke.katalyst.data.structures.lists

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.status.StatusException
import jonathanlocke.katalyst.status.StatusList
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SafeListTest {

    @Test
    fun testWarning() {
        val statuses = StatusList()
        val list = safeList<Int>(warningSize = count(10), statusHandler = statuses)
        list.addAll(1..100)
        assert(statuses.size == 1)
        assert(statuses.isValid())
        assert(statuses.warnings() == 1)
        assert(statuses.errors() == 0)
    }

    @Test
    fun testError() {

        assertThrows<StatusException> {
            val list = safeList<Int>(maximumSize = count(10))
            list.addAll(1..100)
        }
    }
}
package jonathanlocke.katalyst.data.structures.sets

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeSet
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.status.StatusException
import jonathanlocke.katalyst.status.StatusList
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SafeSetTest {

    @Test
    fun testWarning() {
        val statuses = StatusList()
        val set = safeSet<Int>(warningSize = count(10), statusHandler = statuses)
        set.addAll(1..100)
        assert(statuses.size == 1)
        assert(statuses.isValid())
        assert(statuses.warnings() == 1)
        assert(statuses.errors() == 0)
    }

    @Test
    fun testError() {
        assertThrows<StatusException> {
            val set = safeSet<Int>(maximumSize = count(10))
            set.addAll(1..100)
        }
    }
}
package jonathanlocke.katalyst.data.structures.maps

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeMap
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.ProblemException
import jonathanlocke.katalyst.problems.ProblemList
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SafeMapTest {

    @Test
    fun testWarning() {
        val problems = ProblemList()
        val map = safeMap<Int, String>(warningSize = count(10), problemHandler = problems)
        (1..100).forEach { map[it] = it.toString() }
        assert(problems.size == 1)
        assert(problems.isValid())
        assert(problems.warnings() == 1)
        assert(problems.errors() == 0)
    }

    @Test
    fun testError() {
        assertThrows<ProblemException> {
            val map = safeMap<Int, String>(maximumSize = count(10))
            (1..100).forEach { map[it] = it.toString() }
        }
    }
}
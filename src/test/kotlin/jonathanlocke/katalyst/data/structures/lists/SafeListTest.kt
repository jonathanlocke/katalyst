package jonathanlocke.katalyst.data.structures.lists

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeMutableList
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.problems.ProblemException
import jonathanlocke.katalyst.problems.ProblemList
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SafeListTest {

    @Test
    fun testWarning() {
        val problems = ProblemList()
        val list = safeMutableList<Int>(warningSize = count(10), problemListener = problems)
        list.addAll(1..100)
        assert(problems.size == 1)
        assert(problems.isValid())
        assert(problems.warnings() == 1)
        assert(problems.errors() == 0)
    }

    @Test
    fun testError() {

        assertThrows<ProblemException> {
            val list = safeMutableList<Int>(maximumSize = count(10))
            list.addAll(1..100)
        }
    }
}
package jonathanlocke.katalyst.nucleus.data.structures.lists

import jonathanlocke.katalyst.nucleus.data.structures.SafeDataStructure.Companion.safeMutableList
import jonathanlocke.katalyst.nucleus.data.values.count.Count.Companion.count
import jonathanlocke.katalyst.nucleus.problems.ProblemList
import kotlin.test.Test

class SafeMutableListTest {

    @Test
    fun testWarning() {

        val problems = ProblemList()
        val list = safeMutableList<Int>(warningSize = count(10), problemListener = problems)
        list.addAll(1..100)
        assert(problems.size == 1)
        assert(problems.isValid)
        assert(problems.warnings == count(1))
    }
}
package jonathanlocke.katalyst.problems.lists

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemList

class NullProblemList : ProblemList() {

    override val size = 0
    override fun add(index: Int, element: Problem) {}
}
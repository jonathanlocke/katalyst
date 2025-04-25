package jonathanlocke.katalyst.logging.loggers.contextual

import jonathanlocke.katalyst.problems.Problem

class NullProblemList : AbstractMutableList<Problem>() {

    override val size = 0

    override fun get(index: Int): Problem = throw UnsupportedOperationException()
    override fun set(index: Int, element: Problem): Problem = throw UnsupportedOperationException()
    override fun removeAt(index: Int): Problem = throw UnsupportedOperationException()
    override fun add(index: Int, element: Problem) {
    }
}
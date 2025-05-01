package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.categories.Failure

abstract class ProblemHandlerBase() : ProblemHandler {

    private val problems = lazy { ProblemList() }

    override fun problems(): ProblemList = problems.value

    final override fun receive(problem: Problem) {
        if (problem is Failure) fail("Failure encountered")
        onReceive(problem)
    }

    abstract fun onReceive(problem: Problem)
}

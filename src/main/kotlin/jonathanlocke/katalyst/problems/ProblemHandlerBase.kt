package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.categories.Failure

abstract class ProblemHandlerBase() : ProblemHandler {

    private val problems = lazy { ProblemList() }

    override fun problems(): ProblemList = problems.value

    final override fun handle(problem: Problem) {
        problems.value.add(problem)
        if (problem is Failure) fail("Failure encountered")
        onHandle(problem)
    }

    abstract fun onHandle(problem: Problem)
}

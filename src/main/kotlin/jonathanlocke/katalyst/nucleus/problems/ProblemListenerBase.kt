package jonathanlocke.katalyst.nucleus.problems

import jonathanlocke.katalyst.nucleus.problems.categories.Failure

abstract class ProblemListenerBase(override val problems: ProblemList = ProblemList()) : ProblemListener {

    final override fun problem(problem: Problem) {
        if (problem is Failure) fail("Failure encountered")
        onProblem(problem)
    }

    abstract fun onProblem(problem: Problem)
}

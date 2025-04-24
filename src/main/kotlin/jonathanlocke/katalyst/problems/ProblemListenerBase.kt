package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.categories.Failure

abstract class ProblemListenerBase() : ProblemListener {

    override val problems: ProblemList by lazy { ProblemList() }

    final override fun receive(problem: Problem) {
        if (problem is Failure) fail("Failure encountered")
        onReceive(problem)
    }

    abstract fun onReceive(problem: Problem)
}

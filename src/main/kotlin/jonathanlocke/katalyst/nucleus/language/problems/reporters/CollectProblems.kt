package jonathanlocke.katalyst.nucleus.language.problems.reporters

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter

class CollectProblems<Value : Any>(val problems: MutableList<Problem>) : ProblemReporter<Value> {

    override fun report(problem: Problem): Value? {
        problems.add(problem)
        return null
    }
}
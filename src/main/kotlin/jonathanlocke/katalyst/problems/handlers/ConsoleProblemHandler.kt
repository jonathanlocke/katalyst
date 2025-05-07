package jonathanlocke.katalyst.problems.handlers

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemHandlerBase

class ConsoleProblemHandler : ProblemHandlerBase() {
    override fun onReceive(problem: Problem) {
        println(problem)
    }
}
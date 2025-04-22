package jonathanlocke.katalyst.nucleus.language.problems.listeners

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener

class ProblemList<Value : Any>(val problems: MutableList<Problem>) : ProblemListener<Value> {

    operator fun get(index: Int): Problem = problems[index]
    
    override fun problem(problem: Problem): Value? {
        problems.add(problem)
        return null
    }
}
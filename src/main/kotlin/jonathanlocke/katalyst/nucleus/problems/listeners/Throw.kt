package jonathanlocke.katalyst.nucleus.problems.listeners

import jonathanlocke.katalyst.nucleus.problems.Problem
import jonathanlocke.katalyst.nucleus.problems.ProblemException
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.ProblemListenerBase

/**
 * [ProblemListener] that throws an exception.
 *
 * ```
 * text.convert(ToInt(), Throw()))
 * ```
 *
 * Note: Throw() is the default in most cases, as in this case.
 *
 * @see ProblemListener
 * @see ProblemListenerBase
 * @see ProblemException
 * @see Problem
 */
class Throw : ProblemListenerBase() {
    override fun onProblem(problem: Problem) = throw ProblemException(problem)
}

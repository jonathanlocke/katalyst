package jonathanlocke.katalyst.nucleus.language.problems.listeners

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemException
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListenerBase

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

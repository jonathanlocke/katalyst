package jonathanlocke.katalyst.nucleus.language.problems.listeners

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListenerBase
import jonathanlocke.katalyst.nucleus.language.problems.categories.Failure

/**
 * [ProblemListener] that allows the caller to proceed and return some value (or no value), *unless* a [Failure] is
 * encountered. When a failure is received, this listener will throw an exception.
 *
 * ```
 * text.convert(ToInt(), Return())
 * ```
 *
 * @see ProblemListener
 * @see ProblemListenerBase
 * @see Problem
 */
class Return : ProblemListenerBase() {
    override fun onProblem(problem: Problem) {
    }
}
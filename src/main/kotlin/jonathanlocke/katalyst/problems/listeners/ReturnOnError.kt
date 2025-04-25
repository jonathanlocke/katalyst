package jonathanlocke.katalyst.problems.listeners

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.ProblemListenerBase
import jonathanlocke.katalyst.problems.categories.Failure

/**
 * [ProblemListener] that allows the caller to proceed and return some value (or no value), *unless* a [Failure] is
 * encountered. When a failure is received, this listener will throw an exception.
 *
 * ```
 * text.convert(ToInt(), returnOnError)
 * ```
 *
 * @see ProblemListener
 * @see ProblemListenerBase
 * @see Problem
 */
class ReturnOnError : ProblemListenerBase() {

    companion object {
        val returnOnError = ReturnOnError()
    }

    override fun onReceive(problem: Problem) {
    }
}
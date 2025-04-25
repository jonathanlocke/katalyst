package jonathanlocke.katalyst.problems.listeners

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.Problem.Effect.STOP
import jonathanlocke.katalyst.problems.ProblemException
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.ProblemListenerBase

/**
 * [ProblemListener] that throws an exception.
 *
 * ```
 * text.convert(ToInt(), throwOnError))
 * ```
 *
 * Note: throwOnError is the default in most cases, as in this case.
 *
 * @see ProblemListener
 * @see ProblemListenerBase
 * @see ProblemException
 * @see Problem
 */
class ThrowOnError : ProblemListenerBase() {

    companion object {
        val throwOnError = ThrowOnError()
    }

    override fun onReceive(problem: Problem) {
        if (problem.effect == STOP) throw ProblemException("Halting execution:", problem)
    }
}

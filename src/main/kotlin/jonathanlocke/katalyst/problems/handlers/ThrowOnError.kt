package jonathanlocke.katalyst.problems.handlers

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.Problem.Effect.STOP
import jonathanlocke.katalyst.problems.ProblemException
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.ProblemHandlerBase

/**
 * [ProblemHandler] that throws an exception.
 *
 * ```
 * text.convert(ToInt(), throwOnError))
 * ```
 *
 * Note: throwOnError is the default in most cases, as in this case.
 *
 * @see ProblemHandler
 * @see ProblemHandlerBase
 * @see ProblemException
 * @see Problem
 */
class ThrowOnError : ProblemHandlerBase() {

    override fun onHandle(problem: Problem) {
        if (problem.effect == STOP) throw ProblemException("Halting execution:", problems())
    }
}

package jonathanlocke.katalyst.problems.handlers

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.ProblemHandlerBase
import jonathanlocke.katalyst.problems.categories.Failure

/**
 * [ProblemHandler] that allows the caller to proceed and return some value (or no value), *unless* a [Failure] is
 * encountered. When a failure is received, this handler will throw an exception.
 *
 * ```
 * text.convert(ToInt(), returnOnError)
 * ```
 *
 * @see ProblemHandler
 * @see ProblemHandlerBase
 * @see Problem
 */
class ReturnOnError : ProblemHandlerBase() {

    override fun onReceive(problem: Problem) {
    }
}
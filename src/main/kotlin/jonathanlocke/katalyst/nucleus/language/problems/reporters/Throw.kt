package jonathanlocke.katalyst.nucleus.language.problems.reporters

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemException
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter

/**
 * [ProblemReporter] that throws an exception.
 *
 * ```
 * text.convert(ToInt(), Throw()))
 * ```
 *
 * Note: Throw() is the default in most cases, as in this case.
 *
 * @see ProblemReporter
 * @see ProblemException
 * @see Problem
 */
class Throw<Value : Any> : ProblemReporter<Value> {
    override fun report(problem: Problem): Value? =
        throw ProblemException(problem)
}
package jonathanlocke.katalyst.nucleus.language.problems.reporters

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter

/**
 * [ProblemReporter] that returns null.
 *
 * ```
 * text.convert(ToInt(), ReturnNull()))
 * ```
 *
 * @see ProblemReporter
 * @see Problem
 */
class ReturnNull<Value : Any> : ProblemReporter<Value> {
    override fun report(problem: Problem): Value? = null
}
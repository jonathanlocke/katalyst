package jonathanlocke.katalyst.nucleus.language.functional.reporters

import jonathanlocke.katalyst.nucleus.language.functional.Reporter
import jonathanlocke.katalyst.nucleus.language.problems.Problem

/**
 * Error handler that returns null.
 *
 * ```
 * text.convert(ToInt(), ReturnResult()))
 * ```
 */
class ReturnNull<Value : Any> : Reporter<Value> {
    override fun report(problem: Problem): Value? = null
}
package jonathanlocke.katalyst.nucleus.language.problems.listeners

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener

/**
 * [ProblemListener] that returns null.
 *
 * ```
 * text.convert(ToInt(), ReturnNull()))
 * ```
 *
 * @see ProblemListener
 * @see Problem
 */
class ReturnNull<Value : Any> : ProblemListener<Value> {
    override fun problem(problem: Problem): Value? = null
}
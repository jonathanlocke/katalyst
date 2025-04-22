package jonathanlocke.katalyst.nucleus.language.problems.listeners

import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemException
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener

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
 * @see ProblemException
 * @see Problem
 */
class Throw<Value : Any> : ProblemListener<Value> {
    override fun problem(problem: Problem): Value? =
        throw ProblemException(problem)
}
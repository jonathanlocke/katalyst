package jonathanlocke.katalyst.nucleus.language.functional.reporters

import jonathanlocke.katalyst.nucleus.language.functional.Reporter
import jonathanlocke.katalyst.nucleus.language.problems.Problem
import jonathanlocke.katalyst.nucleus.language.problems.ProblemException

/**
 * Error handler that throws an exception
 *
 * ```
 * text.convert(ToInt(), errorHandler = Throw()))
 * ```
 *
 * Note: Throw() is the default, so explicitly setting the error handler in a conversion is not necessary.
 */
class Throw<Value : Any> : Reporter<Value> {
    override fun report(problem: Problem): Value? =
        throw ProblemException(problem)
}
package jonathanlocke.katalyst.nucleus.language.problems

import java.util.function.IntFunction

/**
 * A [MutableList] of [Problem]s.
 *
 * @see Problem
 */
class ProblemList(
    private val problemList: MutableList<Problem> = mutableListOf()
) : MutableList<Problem> by problemList, ProblemListener {

    @Suppress("OVERRIDE_DEPRECATION")
    override fun <Problem> toArray(generator: IntFunction<Array<Problem?>?>): Array<Problem?>? =
        throw UnsupportedOperationException()

    override val problems: ProblemList = this

    override fun problem(problem: Problem) {
        problems.add(problem)
    }
}

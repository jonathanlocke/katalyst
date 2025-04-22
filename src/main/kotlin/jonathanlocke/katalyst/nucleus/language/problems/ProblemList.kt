package jonathanlocke.katalyst.nucleus.language.problems

import jonathanlocke.katalyst.nucleus.language.problems.categories.Error
import jonathanlocke.katalyst.nucleus.language.problems.categories.Warning
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.toCount
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

    val errors = problems.filter { it is Error }.size.toCount()
    val warnings = problems.filter { it is Warning }.size.toCount()
    val isValid = errors.isZero()
    val isInvalid = problems.isNotEmpty()

    override fun problem(problem: Problem) {
        problems.add(problem)
    }
}

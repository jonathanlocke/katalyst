package jonathanlocke.katalyst.nucleus.problems

import jonathanlocke.katalyst.nucleus.data.structures.SafeDataStructure.Companion.safeMutableList
import jonathanlocke.katalyst.nucleus.data.values.count.Count.Companion.toCount
import jonathanlocke.katalyst.nucleus.problems.categories.Error
import jonathanlocke.katalyst.nucleus.problems.categories.Warning
import java.util.function.IntFunction

/**
 * A [MutableList] of [Problem]s.
 *
 * @see Problem
 */
class ProblemList(
    private val problemList: MutableList<Problem> = safeMutableList("problems")
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

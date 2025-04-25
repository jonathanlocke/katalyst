package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.countMaximum
import jonathanlocke.katalyst.problems.Problem.Effect.CONTINUE
import jonathanlocke.katalyst.problems.Problem.Effect.STOP

/**
 * A [MutableList] of [Problem]s.
 *
 * @see Problem
 */
class ProblemList(
    val maximumProblems: Count = countMaximum(),
    private val problemList: MutableList<Problem> = safeList("problems", maximumSize = maximumProblems)
) : MutableList<Problem> by problemList, ProblemListener {

    override fun problems() = this

    fun errors() = problemList.filter { it.effect == STOP }.size
    fun warnings() = problemList.filter { it.effect == CONTINUE }.size
    fun isValid() = errors() == 0
    fun isInvalid() = !isValid()
}

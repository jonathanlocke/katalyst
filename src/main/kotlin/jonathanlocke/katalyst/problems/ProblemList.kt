package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeMutableList
import jonathanlocke.katalyst.problems.Problem.Effect.CONTINUE
import jonathanlocke.katalyst.problems.Problem.Effect.STOP

/**
 * A [MutableList] of [Problem]s.
 *
 * @see Problem
 */
class ProblemList(
    private val problemList: MutableList<Problem> = safeMutableList("problems")
) : MutableList<Problem> by problemList, ProblemListener {

    override val problems: ProblemList = this

    fun errors() = problemList.filter { it.effect == STOP }.size
    fun warnings() = problemList.filter { it.effect == CONTINUE }.size
    fun isValid() = errors() == 0
    fun isInvalid() = !isValid()
}

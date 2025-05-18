package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.globalMaximumSize
import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.problems.Problem.Effect.CONTINUE
import jonathanlocke.katalyst.problems.Problem.Effect.STOP
import jonathanlocke.katalyst.problems.ProblemFormatters.Companion.problemListDetailsFormatter
import jonathanlocke.katalyst.text.formatting.Formattable

/**
 * A [MutableList] of [Problem]s.
 *
 * @see Problem
 */
open class ProblemList(
    val maximumProblems: Count = globalMaximumSize,
) : AbstractMutableList<Problem>(), ProblemHandler, Formattable<ProblemList> {

    private val problemList: MutableList<Problem> by lazy { safeList("problems", maximumSize = maximumProblems) }

    override fun problems() = this
    override fun handlers(): MutableList<ProblemHandler> = throw unimplemented()
    override fun handle(problem: Problem) {
        problemList.add(problem)
    }

    fun count() = problemList.size
    fun errors() = problemList.filter { it.effect == STOP }.size
    fun warnings() = problemList.filter { it.effect == CONTINUE }.size
    fun isValid() = errors() == 0
    fun isInvalid() = !isValid()

    override fun toString(): String = problemListDetailsFormatter.format(this)

    override fun set(index: Int, element: Problem): Problem = problemList.set(index, element)
    override fun removeAt(index: Int): Problem = problemList.removeAt(index)
    override fun add(index: Int, element: Problem) = problemList.add(index, element)
    override val size: Int get() = problemList.size
    override fun get(index: Int): Problem = problemList.get(index)
}

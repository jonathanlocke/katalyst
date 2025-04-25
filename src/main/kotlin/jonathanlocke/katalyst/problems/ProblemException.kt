package jonathanlocke.katalyst.problems

/**
 * An exception that includes a [ProblemList]
 *
 * @property problems The problems that occurred
 *
 * @see Problem
 */
class ProblemException(override val message: String, val problems: List<Problem>) : Exception(
    "$message\n\n${problems.size} problem(s) occurred:\n\n${problems.joinToString()}",
    problems.firstOrNull()?.cause
) {
    constructor(message: String, problem: Problem) : this(message, ProblemList().apply { add(problem) })
}

package jonathanlocke.katalyst.nucleus.language.problems

/**
 * An exception that includes a [ProblemList]
 *
 * @property problems The problems that occurred
 *
 * @see Problem
 */
class ProblemException(val problems: ProblemList) : Exception(
    "${problems.size} problem(s) occurred:\n\n${problems.joinToString()}", problems[0].cause
) {
    constructor(problem: Problem) : this(ProblemList().apply { add(problem) })
}

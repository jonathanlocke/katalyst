package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.ProblemFormatters.Companion.problemListDetailsFormatter

/**
 * An exception that includes a [ProblemList]
 *
 * @property problems The problems that occurred
 *
 * @see Problem
 */
class ProblemException(override val message: String, val problems: ProblemList) : Exception(
    "$message\n\n${problems.size} problem(s) occurred:\n\n${problems.format(problemListDetailsFormatter)}",
    problems.firstOrNull()?.cause
)
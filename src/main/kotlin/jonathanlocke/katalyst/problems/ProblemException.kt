package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.problems.ProblemFormatters.Companion.problemListDetailsFormatter

/**
 * An exception that optionally includes a [ProblemList]
 *
 * @see Problem
 */
class ProblemException(override val message: String, override val cause: Throwable? = null) : Exception(message) {

    constructor(message: String, problems: ProblemList) : this(
        "$message\n\n${problems.size} problem(s) occurred:\n\n${problems.format(problemListDetailsFormatter)}",
        problems.firstOrNull()?.cause
    )

    companion object {

        fun fail(message: String, problems: ProblemList): ProblemException {
            throw ProblemException(message, problems)
        }

        fun fail(message: String, throwable: Throwable): ProblemException {
            throw ProblemException(message, throwable)
        }

        fun fail(message: String): ProblemException {
            throw ProblemException(message)
        }
    }
}


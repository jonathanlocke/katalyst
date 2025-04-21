package jonathanlocke.katalyst.nucleus.language.problems

/**
 * An exception that includes [Problem] information.
 *
 * @param problem The problem that occurred
 *
 * @see Problem
 */
class ProblemException(val problem: Problem) : Exception("A problem occurred: $problem", problem.cause)

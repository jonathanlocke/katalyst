package jonathanlocke.katalyst.nucleus.language.problems

/**
 * An exception that includes [Problem] information.
 *
 * @param problems The problems that occurred
 *
 * @see Problem
 */
class ProblemException(val problems: List<Problem>) : Exception("A problem occurred: $problems[0]", problems[0].cause) {
    constructor(problem: Problem) : this(listOf(problem))
}
package jonathanlocke.katalyst.nucleus.language.problems

import jonathanlocke.katalyst.nucleus.language.problems.listeners.ProblemList

/**
 * An exception that includes [Problem] information.
 *
 * @param problems The problems that occurred
 *
 * @see Problem
 */
class ProblemException(val problems: ProblemList<*>) :
    Exception("A problem occurred: $problems[0]", problems[0].cause)

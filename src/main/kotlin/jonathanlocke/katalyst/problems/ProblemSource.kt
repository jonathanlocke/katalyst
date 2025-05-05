package jonathanlocke.katalyst.problems

/**
 * A problem handler that repeats all the problems it receives to a list of handlers
 */
interface ProblemSource : ProblemHandler {
    fun problemHandlers(): List<ProblemHandler>
}
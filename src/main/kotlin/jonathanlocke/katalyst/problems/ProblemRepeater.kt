package jonathanlocke.katalyst.problems

/**
 * A problem handler that repeats all the problems it receives to a list of handlers
 */
open class ProblemRepeater : ProblemHandlerBase(), ProblemSource {

    private val problemHandlers: MutableList<ProblemHandler> = ArrayList()

    override fun handlers(): MutableList<ProblemHandler> = problemHandlers

    override fun onHandle(problem: Problem) {
        for (it in problemHandlers) it.handle(problem)
    }
}
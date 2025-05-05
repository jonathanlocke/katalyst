package jonathanlocke.katalyst.problems

/**
 * A problem handler that repeats all the problems it receives to a list of handlers
 */
class ProblemRepeater : ProblemHandlerBase(), ProblemSource {
    
    private val problemHandlers: MutableList<ProblemHandler> = ArrayList()

    override fun problemHandlers(): List<ProblemHandler> {
        return problemHandlers
    }

    override fun onReceive(problem: Problem) {
        for (handler in problemHandlers) {
            handler.receive(problem)
        }
    }
}
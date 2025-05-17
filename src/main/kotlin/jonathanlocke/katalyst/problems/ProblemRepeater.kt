package jonathanlocke.katalyst.problems

/**
 * A problem handler that repeats all the problems it receives to a list of handlers
 */
open class ProblemRepeater : ProblemHandlerBase(), ProblemSource {

    private val handlers = ArrayList<ProblemHandler>()

    override fun onHandle(problem: Problem) {
        handlers.forEach { it.handle(problem) }
    }

    override fun handlers() = handlers
}

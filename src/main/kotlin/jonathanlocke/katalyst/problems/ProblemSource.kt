package jonathanlocke.katalyst.problems

/**
 * A problem handler that repeats all the problems it receives to a list of handlers
 */
class ProblemSource : ProblemHandlerBase() {

    val problemHandlers = mutableListOf<ProblemHandler>()

    override fun onReceive(problem: Problem) {
        problemHandlers.forEach { it.receive(problem) }
    }
}
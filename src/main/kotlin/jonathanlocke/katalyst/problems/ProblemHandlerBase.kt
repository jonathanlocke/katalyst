package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.logging.LoggerFactory.Companion.defaultLoggerFactory
import jonathanlocke.katalyst.problems.categories.Failure

open class ProblemHandlerBase() : ProblemHandler {

    private val problems = lazy { ProblemList() }
    private val handlers = ArrayList<ProblemHandler>()

    override fun problems(): ProblemList = problems.value
    override fun handlers() = handlers

    final override fun handle(problem: Problem) {

        // If there are no handlers,
        if (handlers().isEmpty()) {

            // log a warning because the problem will be lost.
            defaultLoggerFactory.newLogger().warning("Unhandled problem: $problem")
        }

        // If the problem is a failure, throw an exception.
        if (problem is Failure) fail("Failure encountered")

        // Add the problem to the list of problems.
        problems().add(problem)

        // Call any subclass to handle the problem,
        onHandle(problem)

        // then call each subscribed handler
        handlers.forEach { it.handle(problem) }
    }

    open fun onHandle(problem: Problem) {
    }
}

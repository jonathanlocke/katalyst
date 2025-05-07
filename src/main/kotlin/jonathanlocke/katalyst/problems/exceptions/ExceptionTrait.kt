package jonathanlocke.katalyst.problems.exceptions

import jonathanlocke.katalyst.problems.ProblemHandler
import java.util.function.Supplier

/**
 * Trait for making exception handling more concise
 */
class ExceptionTrait {

    /**
     * Run the given code, catch any exceptions and report them as errors
     * @param code The code to run
     */
    fun <Value> tryCatch(problemHandler: ProblemHandler, code: Supplier<Value?>): Value? {
        try {
            return code.get()
        } catch (e: Exception) {
            problemHandler.error("Caught exception", e)
            return null
        }
    }

    /**
     * Run the given code, catch any exceptions and report them as errors
     * @param code The code to run
     */
    fun tryCatch(problemHandler: ProblemHandler, code: Runnable) {
        try {
            code.run()
        } catch (e: Exception) {
            problemHandler.error("Caught exception", e)
        }
    }
}
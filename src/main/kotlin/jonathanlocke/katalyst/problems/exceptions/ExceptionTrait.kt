package jonathanlocke.katalyst.problems.exceptions

import jonathanlocke.katalyst.problems.ProblemListener
import java.util.function.Supplier

/**
 * Trait for making exception handling more concise
 */
class ExceptionTrait {

    /**
     * Run the given code, catch any exceptions and report them as errors
     * @param code The code to run
     */
    fun <T> tryCatch(problemListener: ProblemListener, code: Supplier<T?>): T? {
        try {
            return code.get()
        } catch (e: Exception) {
            problemListener.error("Caught exception", e)
            return null
        }
    }

    /**
     * Run the given code, catch any exceptions and report them as errors
     * @param code The code to run
     */
    fun tryCatch(problemListener: ProblemListener, code: Runnable) {
        try {
            code.run()
        } catch (e: Exception) {
            problemListener.error("Caught exception", e)
        }
    }
}
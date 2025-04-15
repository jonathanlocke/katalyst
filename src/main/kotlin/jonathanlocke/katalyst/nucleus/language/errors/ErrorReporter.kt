package jonathanlocke.katalyst.nucleus.language.errors

import jonathanlocke.katalyst.nucleus.language.errors.strategies.Throw

interface ErrorReporter<T> {

    fun errorHandlingStrategy(): ErrorHandlingStrategy<T> = Throw()

    fun error(message: String, throwable: Throwable): T? {
        return errorHandlingStrategy().onError(message, throwable)
    }
}

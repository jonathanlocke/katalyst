package jonathanlocke.katalyst.nucleus.error.strategies

import jonathanlocke.katalyst.nucleus.error.ErrorHandlingStrategy

class Throw<T> : ErrorHandlingStrategy<T> {
    override fun onError(message: String, throwable: Throwable?): T? {
        throw Exception(message, throwable)
    }
}
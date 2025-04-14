package jonathanlocke.katalyst.nucleus.language.errors.strategies

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlingStrategy

class Throw<T> : ErrorHandlingStrategy<T> {
    override fun onError(message: String, throwable: Throwable?): T? {
        throw Exception(message, throwable)
    }
}
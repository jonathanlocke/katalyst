package jonathanlocke.katalyst.nucleus.language.errors.strategies

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlingStrategy

class ReturnNull<T> : ErrorHandlingStrategy<T> {
    override fun onError(message: String, throwable: Throwable?): T? = null
}
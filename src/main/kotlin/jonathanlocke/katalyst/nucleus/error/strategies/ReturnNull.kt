package jonathanlocke.katalyst.nucleus.error.strategies

import jonathanlocke.katalyst.nucleus.error.ErrorHandlingStrategy

class ReturnNull<T> : ErrorHandlingStrategy<T> {
    override fun onError(message: String, throwable: Throwable?): T? = null
}
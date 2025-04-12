package jonathanlocke.katalyst.nucleus.error.strategies

import jonathanlocke.katalyst.nucleus.error.ErrorHandlingStrategy

class ReturnValue<T>(private val errorValue: T) : ErrorHandlingStrategy<T> {
    override fun onError(message: String, throwable: Throwable?): T = errorValue
}
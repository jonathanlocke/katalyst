package jonathanlocke.katalyst.nucleus.language.errors.strategies

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

class ReturnValue<T>(private val errorValue: T) : ErrorHandler<T> {
    override fun error(message: String, throwable: Throwable?): T = errorValue
}
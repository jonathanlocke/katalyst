package jonathanlocke.katalyst.nucleus.language.errors.strategies

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

class ReturnNull<T> : ErrorHandler<T> {
    override fun error(message: String, throwable: Throwable?): T? = null
}
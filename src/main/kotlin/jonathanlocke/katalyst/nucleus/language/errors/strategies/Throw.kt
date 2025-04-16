package jonathanlocke.katalyst.nucleus.language.errors.strategies

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

class Throw<T> : ErrorHandler<T> {
    override fun error(message: String, throwable: Throwable?): T? {
        throw Exception(message, throwable)
    }
}
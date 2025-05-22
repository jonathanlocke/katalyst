package jonathanlocke.katalyst.conversion.converters

import jonathanlocke.katalyst.status.StatusHandler

fun interface ConverterLambda<From, To> {
    fun convert(from: From, statusHandler: StatusHandler): To
}

package jonathanlocke.katalyst.conversion.converters

import jonathanlocke.katalyst.problems.ProblemHandler

fun interface ConverterLambda<From, To> {
    fun convert(from: From, problemHandler: ProblemHandler): To
}
package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface ProblemHandlerMixin : Mixin, ProblemHandler {

    override fun problems() = problemHandler().problems()
    override fun handle(problem: Problem) = problemHandler().handle(problem)
    override fun handlers() = problemHandler().handlers()

    private fun problemHandler() = mixinValue(valueType(ProblemHandler::class), { ProblemHandlerBase() })
}

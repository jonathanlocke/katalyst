package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface ProblemSourceMixin : Mixin, ProblemSource, ProblemHandler {

    override fun problems() = repeater().problems()
    override fun handle(problem: Problem) = repeater().handle(problem)
    override fun handlers() = repeater().handlers()

    private fun repeater() = mixinValue(valueType(ProblemRepeater::class), { ProblemRepeater() })
}

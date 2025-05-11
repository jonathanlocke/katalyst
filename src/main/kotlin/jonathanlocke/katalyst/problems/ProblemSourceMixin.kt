package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface ProblemSourceMixin : Mixin, ProblemSource, ProblemHandler {

    override fun problems() = repeater().problems()
    override fun handlers() = repeater().handlers()
    override fun handle(problem: Problem) = repeater().handle(problem)

    private fun repeater() = mixinValue(valueType(ProblemSource::class), { ProblemRepeater() })
}

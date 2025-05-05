package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface ProblemMixin : Mixin, ProblemSource, ProblemHandler {

    fun problemSource() = mixinValue(valueType(ProblemSource::class), { ProblemRepeater() })

    override fun problems() = problemSource().problems()

    override fun problemHandlers() = problemSource().problemHandlers()

    override fun receive(problem: Problem) {
        problemSource().receive(problem)
    }
}

package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface ProblemMixin : Mixin, ProblemHandler {

    fun repeater() = mixinValue(valueType(ProblemSource::class), { ProblemSource() })
    fun problemHandlers() = repeater().problemHandlers
    override fun receive(problem: Problem) {
        repeater().receive(problem)
    }
}

package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface LoggerMixin : Mixin<Logger>, Logger {
    fun log(problem: Problem) = logger().receive(problem)
    fun logger(): Logger = mixinValue(valueType(Logger::class)) { LoggerFactory.defaultLoggerFactory.newLogger() }
}
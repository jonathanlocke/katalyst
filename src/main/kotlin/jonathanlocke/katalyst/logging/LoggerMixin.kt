package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.LoggerFactory.Companion.defaultLoggerFactory
import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface LoggerMixin : Mixin, Logger {

    fun logger(): Logger = mixinValue(valueType(Logger::class)) { defaultLoggerFactory.newLogger() }

    override fun handle(problem: Problem) = logger().handle(problem)
    override fun logs(): List<Log> = logger().logs()
    override fun problems() = logger().problems()
}
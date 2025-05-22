package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.logging.LoggerFactory.Companion.defaultLoggerFactory
import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.status.Status

interface LoggerMixin : Mixin, Logger {

    fun logger(): Logger = mixinValue(valueType(Logger::class)) { defaultLoggerFactory.newLogger() }

    override fun handle(status: Status) = logger().handle(status)
    override fun logs(): List<Log> = logger().logs()
    override fun statuses() = logger().statuses()
}

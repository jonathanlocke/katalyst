package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

interface StatusHandlerMixin : Mixin, StatusHandler {

    override fun statuses() = statusHandler().statuses()
    override fun handle(status: Status) = statusHandler().handle(status)
    override fun handlers() = statusHandler().handlers()

    private fun statusHandler() = mixinValue(valueType(StatusHandler::class), { StatusHandlerBase() })
}

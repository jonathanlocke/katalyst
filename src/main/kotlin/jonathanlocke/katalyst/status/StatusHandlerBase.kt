package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.logging.LoggerFactory.Companion.defaultLoggerFactory
import jonathanlocke.katalyst.status.categories.Failure

open class StatusHandlerBase : StatusHandler {

    private val statuses = lazy { StatusList() }
    private val handlers = ArrayList<StatusHandler>()

    override fun statuses(): StatusList = statuses.value
    override fun handlers() = handlers

    final override fun handle(status: Status): Boolean {

        // If the status is a failure, throw an exception.
        if (status is Failure) fail("Failure encountered")

        // Add the status to the list of statuses.
        statuses().add(status)

        // Call any subclass to handle the status,
        var handled = onHandle(status)

        // then call each subscribed handler
        handlers.forEach { handled = it.handle(status) || handled }

        // log a warning because the status will be lost.
        if (!handled) {
            defaultLoggerFactory.newLogger().warning("Unhandled problem: $status")
        }

        return handled
    }

    open fun onHandle(status: Status) = false
}

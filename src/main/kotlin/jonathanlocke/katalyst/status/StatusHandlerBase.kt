package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.logging.LoggerFactory.Companion.defaultLoggerFactory
import jonathanlocke.katalyst.status.categories.Failure

open class StatusHandlerBase() : StatusHandler {

    private val statuses = lazy { StatusList() }
    private val handlers = ArrayList<StatusHandler>()

    override fun statuses(): StatusList = statuses.value
    override fun handlers() = handlers

    final override fun handle(status: Status) {

        // If there are no handlers,
        if (handlers().isEmpty()) {

            // log a warning because the status will be lost.
            defaultLoggerFactory.newLogger().warning("Unhandled problem: $status")
        }

        // If the status is a failure, throw an exception.
        if (status is Failure) fail("Failure encountered")

        // Add the status to the list of statuses.
        statuses().add(status)

        // Call any subclass to handle the status,
        onHandle(status)

        // then call each subscribed handler
        handlers.forEach { it.handle(status) }
    }

    open fun onHandle(status: Status) {
    }
}

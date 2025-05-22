package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.status.StatusFormatters.statusListDetailsFormatter

/**
 * An exception that optionally includes a [StatusList]
 *
 * @see Status
 */
class StatusException(override val message: String, override val cause: Throwable? = null) : Exception(message) {

    constructor(message: String, statuses: StatusList) : this(
        "$message\n\n${statuses.summary()} occurred:\n\n${statuses.format(statusListDetailsFormatter)}",
        statuses.firstOrNull()?.cause
    )

    companion object {

        fun fail(message: String, statuses: StatusList): StatusException {
            throw StatusException(message, statuses)
        }

        fun fail(message: String, throwable: Throwable): StatusException {
            throw StatusException(message, throwable)
        }

        fun fail(message: String): StatusException {
            throw StatusException(message)
        }
    }
}


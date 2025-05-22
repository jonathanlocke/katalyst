package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.text.formatting.Formatter

object StatusFormatters {

    val statusMessageFormatter = Formatter<Status> { it.message }

    val statusDetailsFormatter = Formatter<Status> { it.toString() }

    val statusListMessageFormatter = Formatter<StatusList> {
        it.joinToString { statusMessageFormatter.format(it) }
    }

    val statusListDetailsFormatter = Formatter<StatusList> {
        it.joinToString { statusDetailsFormatter.format(it) }
    }

    val statusListStatisticsFormatter = Formatter<StatusList> {
        if (it.isValid()) "Valid (${it.warnings()} warnings)"
        else "Invalid (${it.errors()} errors, ${it.warnings()} warnings)"
    }
}

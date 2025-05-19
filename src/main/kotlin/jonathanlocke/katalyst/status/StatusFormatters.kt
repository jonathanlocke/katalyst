package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.text.formatting.Formatter

class StatusFormatters {

    companion object {

        val statusMessageFormatter = Formatter<Status> { it.message }

        val statusDetailsFormatter = Formatter<Status> { it.toString() }

        val statusListMessageFormatter = Formatter<StatusList> { it ->
            it.joinToString { statusMessageFormatter.format(it) }
        }

        val statusListDetailsFormatter = Formatter<StatusList> { it ->
            it.joinToString { statusDetailsFormatter.format(it) }
        }

        val statusListStatisticsFormatter = Formatter<StatusList> {
            if (it.isValid()) "Valid (${it.warnings()} warnings)"
            else "Invalid (${it.errors()} errors, ${it.warnings()} warnings)"
        }
    }
}
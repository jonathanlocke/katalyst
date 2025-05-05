package jonathanlocke.katalyst.problems

import jonathanlocke.katalyst.text.formatting.Formatter

class ProblemFormatters {

    companion object {

        val problemMessageFormatter = Formatter<Problem> { it.message }

        val problemDetailsFormatter = Formatter<Problem> { it.toString() }

        val problemListMessageFormatter = Formatter<ProblemList> { it ->
            it.joinToString { problemMessageFormatter.format(it) }
        }

        val problemListDetailsFormatter = Formatter<ProblemList> { it ->
            it.joinToString { problemDetailsFormatter.format(it) }
        }

        val problemListStatisticsFormatter = Formatter<ProblemList> {
            if (it.isValid()) "Valid (${it.warnings()} warnings)"
            else "Invalid (${it.errors()} errors, ${it.warnings()} warnings)"
        }
    }
}
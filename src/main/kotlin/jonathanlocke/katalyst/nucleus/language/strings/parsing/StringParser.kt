package jonathanlocke.katalyst.nucleus.language.strings.parsing

import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter

fun interface StringParser<T : Any> {
    fun parse(string: String, reporter: ProblemReporter<T>?): T
}
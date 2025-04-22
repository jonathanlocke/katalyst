package jonathanlocke.katalyst.nucleus.language.strings.parsing

import jonathanlocke.katalyst.nucleus.problems.ProblemListener

fun interface StringParser<T : Any> {
    fun parse(string: String, listener: ProblemListener?): T
}
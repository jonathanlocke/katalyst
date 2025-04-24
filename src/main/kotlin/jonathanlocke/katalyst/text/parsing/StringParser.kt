package jonathanlocke.katalyst.text.parsing

import jonathanlocke.katalyst.problems.ProblemListener

fun interface StringParser<T : Any> {
    fun parse(string: String, listener: ProblemListener?): T
}
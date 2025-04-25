package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.problems.ProblemListener

interface Logger : ProblemListener {
    fun logs(): List<Log>
}

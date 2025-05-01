package jonathanlocke.katalyst.logging

import jonathanlocke.katalyst.problems.ProblemHandler

interface Logger : ProblemHandler {
    fun logs(): List<Log>
}

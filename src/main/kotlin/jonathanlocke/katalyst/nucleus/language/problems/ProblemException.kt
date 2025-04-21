package jonathanlocke.katalyst.nucleus.language.problems

class ProblemException(val problem: Problem) : Exception("A problem occurred: $problem", problem.cause)

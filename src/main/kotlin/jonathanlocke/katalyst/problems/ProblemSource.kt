package jonathanlocke.katalyst.problems

/**
 * A problem source can be listened to by adding a [ProblemHandler].
 */
interface ProblemSource {
    fun handlers(): MutableList<ProblemHandler>
}

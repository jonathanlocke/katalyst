package jonathanlocke.katalyst.problems

/**
 * A problem listener that repeats all the problems it receives to a list of listeners
 *
 * @param listeners The problem listeners to send problems to
 */
class ProblemRepeater(vararg val listeners: Iterable<ProblemList>) : ProblemListenerBase() {
    override fun onReceive(problem: Problem) {
        listeners.forEach { it.forEach { listener -> listener.receive(problem) } }
    }
}
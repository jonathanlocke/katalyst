package jonathanlocke.katalyst.sequencer.serialization

import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter

/**
 * Visitor pattern for limiting serialization impact for DOS attacks and similar problems in input data
 */
interface SerializationLimiter {

    fun isLimitExceeded(session: SerializationSession, reporter: ProblemReporter<*>): Boolean
}
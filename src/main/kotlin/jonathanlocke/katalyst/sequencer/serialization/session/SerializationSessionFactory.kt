package jonathanlocke.katalyst.sequencer.serialization.session

import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter

/**
 * Creates new [SerializationSession]
 */
interface SerializationSessionFactory {

    /**
     * Creates a new [SerializationSession]
     *
     * @param reporter The reporter for problems that occur during serialization
     * @return The session
     */
    fun newSession(reporter: ProblemReporter<*>): SerializationSession?
}

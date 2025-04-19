package jonathanlocke.katalyst.cripsr.kivakit

import com.telenav.kivakit.annotations.code.quality.TypeQuality

/**
 * A problem object describing an issue with reflection
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE, testing = TESTING_NOT_NEEDED, documentation = DOCUMENTED)
class ReflectionProblem : Problem {
    constructor(message: String?, vararg arguments: Any?) : super(message, arguments)

    constructor(cause: Exception?, message: String?, vararg arguments: Any?) : super(cause, message, arguments)
}

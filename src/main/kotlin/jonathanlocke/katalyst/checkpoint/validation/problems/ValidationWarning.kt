package jonathanlocke.katalyst.checkpoint.validation.problems

import jonathanlocke.katalyst.nucleus.language.problems.Warning

class ValidationWarning(message: String, cause: Throwable? = null, value: Any? = null) :
    Warning(message, cause, value)
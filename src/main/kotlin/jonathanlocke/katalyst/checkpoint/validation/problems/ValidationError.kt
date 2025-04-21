package jonathanlocke.katalyst.checkpoint.validation.problems

import jonathanlocke.katalyst.nucleus.language.problems.categories.Error

class ValidationError(message: String, cause: Throwable? = null, value: Any? = null) :
    Error(message, cause, value)
package jonathanlocke.katalyst.checkpoint.validation.problems

import jonathanlocke.katalyst.nucleus.problems.categories.Error

class ValidationError(message: String, cause: Throwable? = null, value: Any? = null) :
    Error(message, cause, value)
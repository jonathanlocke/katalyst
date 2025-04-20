package jonathanlocke.katalyst.nucleus.language.errors.problems

import jonathanlocke.katalyst.nucleus.language.errors.Problem

class Error(message: String, throwable: Throwable? = null) : Problem(message, throwable)
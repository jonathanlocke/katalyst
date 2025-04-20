package jonathanlocke.katalyst.nucleus.language.errors.problems

import jonathanlocke.katalyst.nucleus.language.errors.Problem

class Warning(message: String, throwable: Throwable? = null) : Problem(message, throwable)
package jonathanlocke.katalyst.nucleus.language.problems

open class Warning(message: String, cause: Throwable? = null, value: Any? = null) : Problem(message, cause, value)
package jonathanlocke.katalyst.checkpoint.validation.problems

import jonathanlocke.katalyst.checkpoint.validation.ValidationProblem

class ValidationWarning<T>(message: String, value: T) : ValidationProblem<T>(message, value)
package jonathanlocke.katalyst.checkpoint.validation.problems

import jonathanlocke.katalyst.checkpoint.validation.ValidationProblem

class ValidationError<T>(message: String, value: T) : ValidationProblem<T>(message, value)
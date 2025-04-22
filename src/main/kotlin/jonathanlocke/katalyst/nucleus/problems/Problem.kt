package jonathanlocke.katalyst.nucleus.problems

import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationError
import jonathanlocke.katalyst.checkpoint.validation.problems.ValidationWarning
import jonathanlocke.katalyst.nucleus.problems.categories.Error
import jonathanlocke.katalyst.nucleus.problems.categories.Warning

/**
 * Base class for different kinds of problems, including:
 *
 *  - [Error]
 *  - [Warning]
 *  - [ValidationError]
 *  - [ValidationWarning]
 *
 * @param message A message describing the problem
 * @param cause Any exception that caused the problem
 * @param value Any value associated with the problem
 * @see Error
 * @see Warning
 * @see ValidationError
 * @see ValidationWarning
 */
abstract class Problem(val message: String, val cause: Throwable? = null, val value: Any? = null)
package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.status.categories.Error
import jonathanlocke.katalyst.status.categories.Warning
import jonathanlocke.katalyst.text.formatting.Formattable
import jonathanlocke.katalyst.validation.status.ValidationError
import jonathanlocke.katalyst.validation.status.ValidationWarning

/**
 * Base class for different kinds of statuses, including:
 *
 *  - [Error]
 *  - [Warning]
 *  - [ValidationError]
 *  - [ValidationWarning]
 *
 * @param message A message describing the status
 * @param cause Any exception that caused the status
 * @param value Any value associated with the status
 * @see Error
 * @see Warning
 * @see ValidationError
 * @see ValidationWarning
 */
abstract class Status(val message: String, val cause: Throwable? = null, val value: Any? = null) :
    Formattable<Status> {

    enum class Effect { STOP, CONTINUE }

    abstract val effect: Effect

    abstract fun prefixed(prefix: String): Status

    override fun toString(): String {
        val causeText = cause?.let { ", cause: $it" } ?: ""
        val valueText = value?.let { ", value: $it" } ?: ""
        return message + causeText + valueText
    }
}

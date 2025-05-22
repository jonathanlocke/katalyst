package jonathanlocke.katalyst.conversion.converters

import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.status.Status
import jonathanlocke.katalyst.status.StatusHandler

/**
 * Base class for implementing a [Converter].
 *
 * The *final* implementation of [convert] in this base class implements logic for handling null values, errors
 * resulting from such conditions and exceptions. This allows the subclass to focus on the conversion logic
 * in [onConvert], where the [From] value is guaranteed to be non-null.
 *
 * **Properties**
 *
 * - [nullAllowed] - True if null input values are allowed
 *
 * **Status Handling**
 *
 * - [handleStatusOf] - Reports a status
 *
 * **Extension Points**
 *
 * - [onConvert] - Abstract method called to convert to type [To] if the [From] value is non-null
 *
 * **Inherited**
 *
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 *
 * @param From The type to convert from
 * @param To The type to convert to
 *
 * @see Converter
 * @see StatusHandler
 * @see Status
 */
abstract class ConverterBase<From : Any, To : Any>(
    override val from: ValueType<From>,
    override val to: ValueType<To>,
) : Converter<From, To>, StatusHandler {

    /** True if this converter allows null values */
    val nullAllowed: Boolean = false

    /** The status handler to use when handling conversion problems */
    private val statusHandler = ThreadLocal<StatusHandler>()

    override fun statuses() = statusHandler.get().statuses()

    /**
     * The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
     * error handler returns a null value instead of throwing an exception
     *
     * By default, this returns null. Subclasses should override this method to provide a useful
     * value for null input values.
     */
    override fun nullValue(): To? = null

    /**
     * Invokes the error handler for this object with the given message
     * @param handle The status to report
     */
    override fun handle(status: Status) = statusHandler.get().handle(status)

    /**
     * Converts from the From type to the To type. If the 'from' value is null and the converter allows
     * null values, null will be returned. If the value is null and the converter does not allow null values a status
     * will be broadcast. Any exceptions that occur during conversion are caught and broadcast as status problems.
     */
    final override fun convert(from: From?, statusHandler: StatusHandler): To? {

        // Save the current status handler for this thread
        val currentHandler = this.statusHandler.get()

        try {

            // Set the status handler to use for this thread,
            this.statusHandler.set(statusHandler)

            // then if the value is null,
            return if (from == null) {

                // and we don't allow that,
                if (!nullAllowed) {

                    // then it's an error
                    error("Cannot convert null value")
                    nullValue()

                } else {

                    // otherwise, convert to the null value
                    nullValue()
                }

            } else {

                // and if the value is not null,
                try {

                    // convert to the To type
                    onConvert(from)

                } catch (cause: Exception) {

                    // unless an exception occurs
                    error("Cannot convert $from", cause)
                    nullValue()
                }
            }

        } finally {

            // restore the status handler to the previous value
            this.statusHandler.set(currentHandler)
        }
    }

    /**
     * The method to override to provide the conversion
     */
    protected abstract fun onConvert(from: From): To?
}

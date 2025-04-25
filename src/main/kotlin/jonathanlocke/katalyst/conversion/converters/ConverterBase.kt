package jonathanlocke.katalyst.conversion.converters

import jonathanlocke.katalyst.problems.Problem
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.reflection.ValueType

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
 * **Problems**
 *
 * - [receive] - Reports a problem
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
 * @see ProblemListener
 * @see Problem
 */
abstract class ConverterBase<From : Any, To : Any>(
    override val from: ValueType<From>,
    override val to: ValueType<To>
) :
    Converter<From, To>,
    ProblemListener {

    /** True if this converter allows null values */
    val nullAllowed: Boolean = false

    override val problems: MutableList<Problem> get() = listener.problems

    /** The listener to use when handling conversion problems */
    private lateinit var listener: ProblemListener

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
     * @param receive The problem to report
     */
    override fun receive(problem: Problem) =
        listener.receive(problem)

    /**
     * Converts from the From type to the To type. If the 'from' value is null and the converter allows
     * null values, null will be returned. If the value is null and the converter does not allow null values a problem
     * will be broadcast. Any exceptions that occur during conversion are caught and broadcast as problems.
     */
    final override fun convert(from: From?, listener: ProblemListener): To? {

        // Set the error handler to use for this conversion
        this.listener = listener

        // If the value is null,
        return if (from == null) {

            // and we don't allow that,
            if (!nullAllowed) {

                // then it's an error
                listener.error("Cannot convert null value")
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

            } catch (e: Exception) {

                // unless an exception occurs
                listener.error("Cannot convert $from")
                nullValue()
            }
        }
    }

    /**
     * The method to override to provide the conversion
     */
    protected abstract fun onConvert(from: From): To?
}

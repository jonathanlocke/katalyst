package jonathanlocke.katalyst.convertase.strings

import jonathanlocke.katalyst.convertase.ConverterBase

/**
 *
 * Base class for implementing converters from [String] -> [To]. If the input String is non-null (or nulls
 * are allowed by [nullAllowed]) and if it is non-blank (or blanks are allowed by [blankAllowed]), the
 * subclass method [onToValue] is called to convert the string to the type [To].
 *
 * **Conversion**
 *
 * - [blankAllowed] - True if blank input strings are allowed and should convert to the [nullValue]
 * - [nullAllowed] - True if null input values are allowed
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 *
 * **Errors**
 *
 *  - [errorHandler] - Sets the error handler to use when reporting errors
 *  - [error] - Reports an error
 *
 * **Implementing Converters**
 *
 * - [onToValue] - Called with an always non-null text string to convert if the from string value is non-null (or nulls
 *                 are allowed) and if it is non-blank (or blanks are allowed)
 *
 * * @param To The type to convert to and from
 * @see ConverterBase
 * @see FromStringConverter
 */
abstract class FromStringConverterBase<To : Any> : ConverterBase<String, To>(), FromStringConverter<To> {

    /** True if blank strings are allowed */
    val blankAllowed: Boolean = false

    /**
     * {@inheritDoc}
     */
    override fun onConvert(from: String): To? =

        // If we allow blank strings and the from string is blank,
        if (blankAllowed && from.isBlank()) {

            // then convert to the null value,
            nullValue()

        } else {

            // otherwise, convert to the To type
            onToValue(from)
        }

    /**
     * Implemented by subclass to convert the given string to a value. The subclass implementation will never be called
     * in cases where value is null or blank, so it need not check for either case.
     *
     * @param text The (guaranteed non-null, non-blank) value to convert
     * @return The converted object
     */
    protected abstract fun onToValue(text: String): To?
}

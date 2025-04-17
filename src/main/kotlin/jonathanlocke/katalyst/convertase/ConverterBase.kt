package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler

/**
 * Base class for implementing converters. The [Converter.convert] method converts from the [From] type
 * to the [To] type. Whether the conversion allows null values or not can be specified with [nullAllowed].
 * Errors are handled according to the conventions of [ErrorHandler], which allows the caller to determine
 * if a conversion throws an exception or if it returns null.
 *
 * **Conversion**
 *
 * - [convert] - Converts from the [From] type to the [To] type
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
 * - [onConvert] - Called if the [From] value is non-null to perform the conversion to type [To]
 *
 * @param From The type to convert from
 * @param To The type to convert to
 * @see Converter
 */
abstract class ConverterBase<From : Any, To : Any> : Converter<From, To> {

    /** True if this converter allows null values */
    val nullAllowed: Boolean = false

    override fun nullValue(): To? = null

    /**
     * Converts from the From type to the To type. If the 'from' value is null and the converter allows
     * null values, null will be returned. If the value is null and the converter does not allow null values a problem
     * will be broadcast. Any exceptions that occur during conversion are caught and broadcast as problems.
     */
    override fun convert(from: From?): To? {

        // If the value is null,
        return if (from == null) {

            // and we don't allow that,
            if (!nullAllowed) {

                // then it's an error
                error("Cannot convert null value") ?: nullValue()

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
                error("Cannot convert $from")
            }
        }
    }

    /**
     * The method to override to provide the conversion
     */
    protected abstract fun onConvert(from: From): To?
}

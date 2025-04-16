package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.strategies.Throw

/**
 * Base class for implementing converters. The inherited [Converter.convert] method converts from the 'From' type
 * to the To type. Whether the conversion allows null values or not can be specified with [nullAllowed].
 *
 * **Conversion**
 *
 * - [convert]
 *
 * **Implementing Converters**
 *
 * - [onConvert]
 *
 * **Missing Values**
 *
 * - [nullAllowed]
 * - [nullAllowed]
 *
 * @param From The type to convert from
 * @param To The type to convert to
 * @see Converter
 */
abstract class ConverterBase<From : Any, To : Any> : Converter<From, To> {

    /** True if this converter allows null values */
    val nullAllowed: Boolean = false

    var errorHandler: ErrorHandler<To> = Throw()

    override fun errorHandler(errorHandler: ErrorHandler<To>): Converter<From, To> {
        this.errorHandler = errorHandler
        return this
    }

    override fun error(message: String, throwable: Throwable?): To? = errorHandler.error(message, throwable)

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
                error("Cannot convert null value")

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

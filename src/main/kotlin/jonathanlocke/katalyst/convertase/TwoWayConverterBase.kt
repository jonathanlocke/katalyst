package jonathanlocke.katalyst.convertase

import kotlin.reflect.KClass

/**
 * Base class for implementing two-way converters.
 *
 * Adds to [ConverterBase] an implementation of [unconvert] that checks for null, catches
 * exceptions, and calls [onUnconvert].
 *
 * **Conversions**
 *
 * - [Converter.convert] - Called to convert *from* => *to*
 * - [unconvert] - Called to convert *to* => *from*
 *
 * **Implementing Converters**
 *
 * - [onConvert] - Called to convert *from* => *to*
 * - [onUnconvert] - Called to convert *to* => *from*
 *
 * **Missing Values**
 *
 * - [nullValue]
 *
 * @param From The type to convert from
 * @param To The type to convert to
 */
abstract class TwoWayConverterBase<From : Any, To : Any>(
    fromType: KClass<From>,
    toType: KClass<To>
) : ConverterBase<From, To>(fromType, toType), TwoWayConverter<From, To> {

    /**
     * Converts `value` from the *to* type back to the *from* type.
     *
     * @param to The value to convert back to the *from* type
     * @return The converted value or null if a problem occurs
     */
    override fun unconvert(to: To?): From? {

        // If the value is null
        if (to == null) {

            // and we allow null values
            return if (allowsNull()) {

                // then let the subclass convert to a null logical value
                nullValue()

            } else {

                // otherwise, we can't convert null values
                error("Cannot unconvert null value")
            }
        }

        // Return
        return try {

            // to value converted back to the From type,
            onUnconvert(to)
            
        } catch (e: Exception) {

            // unless there's an error
            error("Cannot unconvert $to")
        }
    }

    /**
     * Returns logical value to use for null.
     */
    protected open fun nullValue(): From? = null

    /**
     * Called to "un-convert" from 'to' to 'from'.
     */
    protected abstract fun onUnconvert(value: To): From
}

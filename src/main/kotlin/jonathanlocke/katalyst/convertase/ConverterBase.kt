package jonathanlocke.katalyst.convertase

import kotlin.reflect.KClass

/**
 * Base class for implementing converters. The inherited [Converter.convert] method converts from the 'From' type
 * to the To type. Whether the conversion allows null values or not can be specified with [allowNull].
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
 * - [allowsNull]
 * - [allowNull]
 *
 * @param From The type to convert from
 * @param To The type to convert to
 * @see Converter
 */
abstract class ConverterBase<From : Any, To : Any>(
    override val fromType: KClass<From>,
    override val toType: KClass<To>
) : Converter<From, To> {

    /** True if this converter allows null values */
    private var allowNull: Boolean = false

    /**
     * Sets whether null values should be converted to null.
     *
     * @param allowNull True if null values should be allowed
     * @return The current instance
     */
    fun allowNull(allowNull: Boolean): ConverterBase<From, To> {
        this.allowNull = allowNull
        return this
    }

    /**
     * Returns true if this converter allows null values, false if a problem will be broadcast when a null value is
     * encountered.
     */
    fun allowsNull(): Boolean = allowNull

    /**
     * Converts from the From type to the To type. If the 'from' value is null and the converter allows
     * null values, null will be returned. If the value is null and the converter does not allow null values a problem
     * will be broadcast. Any exceptions that occur during conversion are caught and broadcast as problems.
     */
    override fun convert(from: From?): To? {

        // If the value is null,
        return if (from == null) {

            // and we don't allow that,
            if (!allowsNull()) {

                // then it's an error
                error("Cannot convert null value to $toType")
                
            } else {

                // otherwise, convert to null
                null
            }
        } else {

            // and if the value is not null,
            try {

                // convert to the To type
                onConvert(from)

            } catch (e: Exception) {

                // unless an exception occurs
                error("Cannot convert $from to $toType")
            }
        }
    }

    /**
     * The method to override to provide the conversion
     */
    protected abstract fun onConvert(from: From): To?
}

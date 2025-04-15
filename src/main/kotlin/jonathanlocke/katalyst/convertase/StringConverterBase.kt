package jonathanlocke.katalyst.convertase

import java.util.*
import java.util.function.Function
import kotlin.reflect.KClass

/**
 * Base class for conversions to and from String objects.
 *
 * <p><b>Converting to and from Strings</b>
 * <p>
 * Subclasses implement conversion by overriding [onToValue]. If no implementation is given for
 * [onToString], a default implementation is provided which simply converts the object to a String by
 * calling [toString].
 * </p>
 *
 * <p><b>Blank Strings</b></p>
 *
 * <p>
 * Just as [ConverterBase] has an option to allow or disallow null values, [StringConverterBase] has an
 * option to allow or disallow blank strings. A blank string is null, zero length or contains nothing but whitespace. The
 * method [allowBlankStrings] can be used to allow blank values (which are not allowed by default), and
 * [allowsBlankString] will return true if the converter allows blank strings.
 * </p>
 *
 * <p><b>Thread Safety</b></p>
 *
 * <p>
 * Note: String converters in general are thread-safe as the base classes do not have mutable state, but some specific
 * string converter implementations may have mutable state, for example, Java formatter objects. Such converters will
 * not be thread-safe.
 * </p>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>[Converter.convert] - Called to convert string => value</li>
 *     <li>[TwoWayConverter.unconvert] - Called to convert value => string</li>
 * </ul>
 *
 * <p><b>Implementing Converters</b></p>
 *
 * <ul>
 *     <li>[onToString] - Overridden to provide value => string conversion</li>
 *     <li>[onToValue] - Overridden to provide string => value conversion</li>
 * </ul>
 *
 * <p><b>Missing Values</b></p>
 *
 * <ul>
 *     <li>[allowsBlankString]</li>
 *     <li>[allowBlankStrings]</li>
 *     <li>[nullString]</li>
 * </ul>
 *
 * @param To The type to convert to and from
 * @see TwoWayConverterBase
 * @see StringConverter
 */
abstract class StringConverterBase<To : Any>(
    toType: KClass<To>,
    private var lambda: Function<String?, To?>? = null
) : TwoWayConverterBase<String, To>(String::class, toType), StringConverter<To> {

    /** True if blank strings are allowed */
    private var allowBlank: Boolean = false

    /**
     * Specifies whether blank (null, whitespace or "") strings should be allowed (they will convert to null)
     */
    fun allowBlankStrings(allowBlank: Boolean): StringConverterBase<To> {
        this.allowBlank = allowBlank
        return this
    }

    /**
     * Returns true if this string converter allows blank strings
     */
    fun allowsBlankString(): Boolean = allowBlank

    /**
     * {@inheritDoc}
     */
    final override fun onConvert(from: String): To? {

        // If we allow blank strings and the from string is blank,
        return if (allowBlank && from.isBlank()) {

            // then convert to null,
            null

        } else {

            // otherwise, convert to the To type
            onToValue(from)
        }
    }

    /**
     * {@inheritDoc}
     */
    final override fun unconvert(to: To?): String? {

        // If the value is null,
        if (to == null) {

            // and we allow that,
            return if (allowsNull()) {

                // then unconvert to the null string value,
                nullString()
                
            } else {

                // otherwise, it's an error
                error("Cannot unconvert null value")
            }
        }

        // Return
        return try {

            // the value as a string
            onToString(to)

        } catch (e: Exception) {

            // unless there's an exception
            error("Cannot unconvert: $to")
        }
    }

    /**
     * Returns the string representation of a null value. By default, this value is null, not "null".
     */
    protected open fun nullString(): String? = null

    /**
     * Convert the given value to a string
     *
     * @param value The (guaranteed non-null, non-blank) value
     * @return A string which is by default value.toString() if this method is not overridden
     */
    protected open fun onToString(value: To): String = value.toString()

    /**
     * Implemented by subclass to convert the given string to a value. The subclass implementation will never be called
     * in cases where value is null or blank, so it need not check for either case.
     *
     * @param value The (guaranteed non-null, non-blank) value to convert
     * @return The converted object
     */
    protected open fun onToValue(value: String?): To? {
        Objects.requireNonNull(lambda)
        return lambda!!.apply(value)
    }
}

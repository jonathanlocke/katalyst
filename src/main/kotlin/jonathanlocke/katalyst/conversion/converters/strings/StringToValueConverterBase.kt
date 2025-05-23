package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.ConverterBase
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueTypeString

/**
 * Base class for implementing converters from [String] -> [Value]. If the input String is non-null (or nulls
 * are allowed by [nullAllowed]) and if it is non-blank (or blanks are allowed by [blankAllowed]), the
 * subclass method [onToValue] is called to convert the string to the type [Value].
 *
 * **Properties**
 *
 * - [blankAllowed] - True if blank input strings are allowed and should convert to the [nullValue]
 *
 * **Methods**
 *
 * - [register] - Registers this converter with the given [ConversionRegistry]
 *
 * **Extension Points**
 *
 * - [onToValue] - Called with an always non-null text string to convert if the from string value is non-null (or nulls
 *                 are allowed) and if it is non-blank (or blanks are allowed)
 *
 * **Inherited**
 *
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 * - [nullAllowed] - True if null input values are allowed
 * - [handleStatusOf] - Reports an error to the error handler
 *
 * @param Value The type to convert to
 *
 * @see ConverterBase
 * @see StringToValueConverter
 */
abstract class StringToValueConverterBase<Value : Any>(override val type: ValueType<Value>) :
    ConverterBase<String, Value>(valueTypeString, type),
    StringToValueConverter<Value> {

    /** True if blank strings are allowed */
    val blankAllowed: Boolean = false

    /**
     * Registers this converter with the [ConversionRegistry]
     */
    override fun register(conversionRegistry: ConversionRegistry) =
        toStringToValueConversion(type).register(conversionRegistry)

    /**
     * {@inheritDoc}
     */
    final override fun onConvert(from: String): Value? =

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
    protected abstract fun onToValue(text: String): Value?
}

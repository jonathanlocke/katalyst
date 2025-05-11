package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.Conversion
import jonathanlocke.katalyst.conversion.ConversionBase
import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.conversion.converters.strings.values.FormatValueToString
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

/**
 * Converter from [String] -> [Value]
 *
 *  - [toStringToValueConversion] - Returns a bidirectional [String] <-> [Value] [Conversion]
 *  - [stringToValueConverter] - Returns a [StringToValueConverter] that converts from [String] to [Value] using the
 *                               given lambda. This makes it more concise to implement a [StringToValueConverter].
 *
 * **Extension Methods**
 *
 *  - [String.convert] - Converts a string to a value
 *  - [String.convertToList] - Converts a textual list to a list of values
 *
 * @param Value The value to convert to
 *
 * @see Conversion
 */
interface StringToValueConverter<Value : Any> : Converter<String, Value> {

    /**
     * The class of [Value]
     */
    val type: ValueType<Value>

    /**
     * Registers this converter with the given [ConversionRegistry]
     */
    fun register(conversionRegistry: ConversionRegistry)

    /**
     * Returns a bidirectional [Conversion] between [String] and [Value] by implementing the forward
     * conversion with this converter and the reverse conversion with a [FormatValueToString] converter.
     */
    fun toStringToValueConversion(type: ValueType<Value>): Conversion<String, Value> =
        object : ConversionBase<String, Value>(valueType(String::class), type) {
            override fun forwardConverter(): Converter<String, Value> = this@StringToValueConverter
            override fun reverseConverter(): Converter<Value, String> = FormatValueToString(type)
        }
}

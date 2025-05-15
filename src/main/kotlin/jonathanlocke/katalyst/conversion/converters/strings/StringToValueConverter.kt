package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.Conversion
import jonathanlocke.katalyst.conversion.ConversionBase
import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.conversion.converters.strings.values.FormatValueToString
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueTypeString

/**
 * Converter from [String] -> [Value]
 *
 *  - [toStringToValueConversion] - Returns a bidirectional [String] <-> [Value] [Conversion]
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
        object : ConversionBase<String, Value>(valueTypeString, type) {
            override fun forwardConverter(): Converter<String, Value> = this@StringToValueConverter
            override fun reverseConverter(): Converter<Value, String> = FormatValueToString(type)
        }
}

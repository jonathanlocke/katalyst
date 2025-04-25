package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.Conversion
import jonathanlocke.katalyst.conversion.ConversionBase
import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.convert
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.convertToList
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.conversion.converters.strings.collections.ListConversion
import jonathanlocke.katalyst.conversion.converters.strings.values.ValueToString
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.Throw
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.text.parsing.Separator

/**
 * Converter from [String] -> [Value]
 *
 *  - [asStringToValueConversion] - Returns a bidirectional [String] <-> [Value] [Conversion]
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
     * conversion with this converter and the reverse conversion with a [ValueToString] converter.
     */
    fun asStringToValueConversion(type: ValueType<Value>): Conversion<String, Value> =
        object : ConversionBase<String, Value>(valueType(String::class), type) {
            override fun forwardConverter(): Converter<String, Value> = this@StringToValueConverter
            override fun reverseConverter(): Converter<Value, String> = ValueToString(type)
        }

    companion object {

        /**
         * Implements a [StringToValueConverter] using the given lambda
         *
         * @param type The class of [Value]
         * @param lambda Converter from String -> [Value]
         */
        fun <Value : Any> stringToValueConverter(
            type: ValueType<Value>,
            lambda: (String, ProblemListener) -> Value?
        ): StringToValueConverter<Value> = (object : StringToValueConverterBase<Value>(type) {
            override fun onToValue(text: String): Value? = lambda.invoke(text, this)
        })

        /**
         * Converts any string to an object of type [Value]
         *
         * @param converter Converter from String -> [Value]
         * @param listener An optional error handler to use
         */
        fun <Value : Any> String.convert(
            converter: StringToValueConverter<Value>,
            listener: ProblemListener = Throw()
        ): Value? = converter.convert(this, listener)

        /**
         * Converts any string to a list of objects of type [Value]
         * @param stringToValueConverter The converter to use to convert each element in the list
         * @param separator The separator to use when parsing text and joining value objects
         * @param listener An optional error handler to use
         * @param elementToValueReporter An optional error handler to use when an element in the list fails to convert
         */
        fun <Value : Any> String.convertToList(
            stringToValueConverter: StringToValueConverter<Value>,
            separator: Separator = Separator(),
            listener: ProblemListener = Throw(),
            elementToValueReporter: ProblemListener = Throw()
        ): List<Value>? =
            ListConversion(stringToValueConverter.type, stringToValueConverter, separator, elementToValueReporter)
                .forwardConverter()
                .convert(this, listener)
    }
}

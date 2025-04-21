package jonathanlocke.katalyst.convertase.conversion.strings

import jonathanlocke.katalyst.convertase.conversion.Conversion
import jonathanlocke.katalyst.convertase.conversion.ConversionBase
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.toList
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.toValue
import jonathanlocke.katalyst.convertase.conversion.strings.collections.ListConversion
import jonathanlocke.katalyst.convertase.conversion.strings.values.StringToNumber
import jonathanlocke.katalyst.convertase.conversion.strings.values.ValueToString
import jonathanlocke.katalyst.nucleus.language.functional.Reporter
import jonathanlocke.katalyst.nucleus.language.functional.reporters.Throw
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator
import kotlin.reflect.KClass

/**
 * Converts from [String] -> [Value]
 *
 *  - [asStringToValueConversion] - Returns a bidirectional [String] <-> [Value] [Conversion]
 *  - [stringToValueConverter] - Returns a [StringToValueConverter] that converts from [String] to [Value] using the
 *                               given lambda. This makes it more concise to implement a [StringToValueConverter].
 *
 * **Extension Methods**
 *
 *  - [String.toValue] - Converts a string to a value
 *  - [String.toList] - Converts a textual list to a list of values
 *
 * @param Value The value to convert to
 *
 * @see Conversion
 * @see StringToNumber.Companion.intConverter
 */
interface StringToValueConverter<Value : Any> : Converter<String, Value> {

    /**
     * The class of [Value] (necessary due to type erasure)
     */
    val valueClass: KClass<Value>

    /**
     * Returns a bidirectional [Conversion] between [String] and [Value] by implementing the forward
     * conversion with this converter and the reverse conversion with a [ValueToString] converter.
     */
    fun asStringToValueConversion(valueClass: KClass<Value>): Conversion<String, Value> =
        object : ConversionBase<String, Value>(String::class, valueClass) {
            override fun forwardConverter(): Converter<String, Value> = this@StringToValueConverter
            override fun reverseConverter(): Converter<Value, String> = ValueToString(valueClass)
        }

    companion object {

        /**
         * Implements a [StringToValueConverter] using the given lambda
         *
         * @param valueClass The class of [Value] (due to type erasure)
         * @param lambda Converter from String -> [Value]
         */
        fun <Value : Any> stringToValueConverter(
            valueClass: KClass<Value>,
            lambda: (String, Reporter<Value>) -> Value?
        ): StringToValueConverter<Value> = (object : StringToValueConverterBase<Value>(valueClass) {
            override fun onToValue(text: String): Value? = lambda.invoke(text, this)
        }).also {
            it.register()
        }

        /**
         * Converts any string to an object of type [Value]
         *
         * @param converter Converter from String -> [Value]
         * @param reporter An optional error handler to use
         */
        fun <Value : Any> String.toValue(
            converter: StringToValueConverter<Value>,
            reporter: Reporter<Value> = Throw()
        ): Value? = converter.convert(this, reporter)

        /**
         * Converts any string to a list of objects of type [Value]
         * @param stringToValueConverter The converter to use to convert each element in the list
         * @param separator The separator to use when parsing text and joining value objects
         * @param reporter An optional error handler to use
         * @param elementToValueReporter An optional error handler to use when an element in the list fails to convert
         */
        fun <Value : Any> String.toList(
            stringToValueConverter: StringToValueConverter<Value>,
            separator: Separator = Separator(),
            reporter: Reporter<List<Value>> = Throw(),
            elementToValueReporter: Reporter<Value> = Throw()
        ): List<Value>? =
            ListConversion(stringToValueConverter.valueClass, stringToValueConverter, separator, elementToValueReporter)
                .forwardConverter()
                .convert(this, reporter)
    }
}

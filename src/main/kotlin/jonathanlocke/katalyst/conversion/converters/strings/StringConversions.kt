package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.converters.strings.collections.ListConversion
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers
import jonathanlocke.katalyst.text.parsing.Separator

class StringConversions {
    companion object {
        /**
         * Implements a [StringToValueConverter] using the given lambda
         *
         * @param type The class of [Value]
         * @param lambda Converter from String -> [Value]
         */
        fun <Value : Any> stringToValueConverter(
            type: ValueType<Value>,
            lambda: (String, StatusHandler) -> Value?,
        ): StringToValueConverter<Value> = (object : StringToValueConverterBase<Value>(type) {
            override fun onToValue(text: String): Value? = lambda.invoke(text, this)
        })

        /**
         * Converts any string to an object of type [Value]
         *
         * @param converter Converter from String -> [Value]
         * @param statusHandler An optional error handler to use
         */
        fun <Value : Any> String.convert(
            converter: StringToValueConverter<Value>,
            statusHandler: StatusHandler = StatusHandlers.Companion.throwOnError,
        ): Value? = converter.convert(this, statusHandler)

        /**
         * Converts any string to a list of objects of type [Value]
         * @param stringToValueConverter The converter to use to convert each element in the list
         * @param separator The separator to use when parsing text and joining value objects
         * @param statusHandler An optional error handler to use
         * @param elementStatusHandler An optional error handler to use when an element in the list fails to convert
         */
        fun <Value : Any> String.convertToList(
            stringToValueConverter: StringToValueConverter<Value>,
            separator: Separator = Separator.Companion.commaSeparator,
            statusHandler: StatusHandler = StatusHandlers.Companion.throwOnError,
            elementStatusHandler: StatusHandler = StatusHandlers.Companion.throwOnError,
        ): List<Value>? =
            ListConversion(stringToValueConverter.type, stringToValueConverter, separator, elementStatusHandler)
                .forwardConverter()
                .convert(this, statusHandler)
    }
}
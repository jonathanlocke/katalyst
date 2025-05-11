package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.converters.strings.collections.ListConversion
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ProblemHandlers
import jonathanlocke.katalyst.reflection.ValueType
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
            lambda: (String, ProblemHandler) -> Value?
        ): StringToValueConverter<Value> = (object : StringToValueConverterBase<Value>(type) {
            override fun onToValue(text: String): Value? = lambda.invoke(text, this)
        })

        /**
         * Converts any string to an object of type [Value]
         *
         * @param converter Converter from String -> [Value]
         * @param problemHandler An optional error handler to use
         */
        fun <Value : Any> String.convert(
            converter: StringToValueConverter<Value>,
            problemHandler: ProblemHandler = ProblemHandlers.Companion.throwOnError
        ): Value? = converter.convert(this, problemHandler)

        /**
         * Converts any string to a list of objects of type [Value]
         * @param stringToValueConverter The converter to use to convert each element in the list
         * @param separator The separator to use when parsing text and joining value objects
         * @param problemHandler An optional error handler to use
         * @param elementProblemHandler An optional error handler to use when an element in the list fails to convert
         */
        fun <Value : Any> String.convertToList(
            stringToValueConverter: StringToValueConverter<Value>,
            separator: Separator = Separator.Companion.commaSeparator,
            problemHandler: ProblemHandler = ProblemHandlers.Companion.throwOnError,
            elementProblemHandler: ProblemHandler = ProblemHandlers.Companion.throwOnError
        ): List<Value>? =
            ListConversion(stringToValueConverter.type, stringToValueConverter, separator, elementProblemHandler)
                .forwardConverter()
                .convert(this, problemHandler)
    }
}
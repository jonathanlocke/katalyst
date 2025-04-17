package jonathanlocke.katalyst.convertase.strings

import jonathanlocke.katalyst.convertase.Conversion
import jonathanlocke.katalyst.convertase.Converter
import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.convert
import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.convertToList
import jonathanlocke.katalyst.convertase.strings.collections.ListConversion
import jonathanlocke.katalyst.convertase.strings.values.ToString
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.handlers.Throw
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator

/**
 * Converts from a string value to a value of type [To].
 *
 * **Extension Methods**
 *
 *  - [String.convert] - Converts a string to a value
 *  - [String.convertToList] - Converts a textual list to a list of values
 *
 * @param To The value to convert to
 * @see Conversion
 */
interface FromStringConverter<To : Any> : Converter<String, To> {

    companion object {

        /**
         * Implements a [FromStringConverter] using the given lambda
         *
         * @param lambda Converter from String -> [To]
         * @param errorHandler An optional error handler to use
         */
        fun <To : Any> fromStringConverter(
            errorHandler: ErrorHandler<To> = Throw(),
            lambda: (String) -> To?
        ): FromStringConverter<To> = object : FromStringConverterBase<To>() {
            override fun onToValue(text: String): To? {
                errorHandler(errorHandler)
                return lambda.invoke(text)
            }
        }

        /**
         * Converts any string to an object of type [To]
         *
         * @param converter Converter from String -> [To]
         * @param errorHandler An optional error handler to use
         */
        fun <To : Any> String.convert(
            converter: FromStringConverter<To>,
            errorHandler: ErrorHandler<To> = Throw()
        ): To? =
            converter.convert(this) ?: errorHandler.error("Cannot convert $this")

        /**
         * Converts any string to a list of objects of type [To]
         *
         * @param fromStringConverter Converter from String -> [To]
         * @param separator Separator to use when parsing
         * @param errorHandler An optional error handler to invoke for each element that fails to convert
         */
        fun <T : Any> String.convertToList(
            fromStringConverter: FromStringConverter<T>,
            separator: Separator = Separator(),
            errorHandler: ErrorHandler<List<T>> = Throw()
        ): List<T>? =
            ListConversion(ToString(), fromStringConverter, separator, "?", errorHandler)
                .converter().convert(this)
    }
}

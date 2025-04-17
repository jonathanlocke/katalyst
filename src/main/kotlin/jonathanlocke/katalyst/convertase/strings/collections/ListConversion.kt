package jonathanlocke.katalyst.convertase.strings.collections

import jonathanlocke.katalyst.convertase.Conversion
import jonathanlocke.katalyst.convertase.Converter
import jonathanlocke.katalyst.convertase.ConverterBase
import jonathanlocke.katalyst.convertase.strings.FromStringConverter
import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.fromStringConverter
import jonathanlocke.katalyst.convertase.strings.ToStringConverter
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.handlers.ReturnNull
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator

/**
 * A [Conversion] between strings and lists.
 *
 * **String -> List<T>**
 *
 * The string is split by [Separator] and each value is converted to an object using the [toStringConverter].
 * The semantics of the converter are preserved: If the converter fails and the [ErrorHandler] is [ReturnNull],
 * then the list element will be null.
 *
 * **List<T> -> String**
 *
 * Each element in the list is mapped to a string with [fromStringConverter] and the result is joined using [Separator].
 * If the converter fails to convert an element in the list and the [ErrorHandler] is [ReturnNull], then the
 * [defaultToStringValue] will be used for that element.
 *
 * @see Conversion
 * @see FromStringConverter
 * @see ToStringConverter
 * @see FromStringConverter
 * @see Separator
 */
class ListConversion<T : Any>(
    val toStringConverter: ToStringConverter<T>,
    val fromStringConverter: FromStringConverter<T>,
    val separator: Separator = Separator(),
    val defaultToStringValue: String = "?",
    val errorHandler: ErrorHandler<List<T>>
) : Conversion<String, List<T>> {

    init {
        errorHandler(errorHandler)
    }

    @Suppress("UNCHECKED_CAST")
    override fun converter(): FromStringConverter<List<T>> = fromStringConverter { it: String ->
        separator.split(it)
            .map { member -> fromStringConverter.convert(member) ?: error("Failed to convert $member") }
            .toSet() as List<T>
    }

    override fun reverseConverter(): Converter<List<T>, String> = object : ConverterBase<List<T>, String>() {
        override fun onConvert(from: List<T>): String =
            separator.join(from.map { toStringConverter.convert(it) ?: defaultToStringValue }.toList())
    }
}

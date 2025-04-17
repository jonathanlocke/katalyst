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
 * A [Conversion] between strings and sets.
 *
 * **String -> Set<T>**
 *
 * The string is split by [Separator] and each value is converted to an object using the [toStringConverter].
 * The semantics of the converter are preserved: If the converter fails and the [ErrorHandler] is [ReturnNull],
 * then the set will contain a null value as a member.
 *
 * **Set<T> -> String**
 *
 * Each member in the set is mapped to a string with [fromStringConverter] and the result is joined using [Separator].
 * If the converter fails to convert a member in the set and the [ErrorHandler] is [ReturnNull], then the
 * [defaultToStringValue] will be used for that member.
 *
 * @see Conversion
 * @see FromStringConverter
 * @see ToStringConverter
 * @see FromStringConverter
 * @see Separator
 */
class SetConversion<T : Any>(
    val toStringConverter: ToStringConverter<T>,
    val fromStringConverter: FromStringConverter<T>,
    val separator: Separator = Separator(),
    val defaultToStringValue: String = "?",
    val errorHandler: ErrorHandler<Set<T>>
) : Conversion<String, Set<T>> {

    init {
        errorHandler(errorHandler)
    }

    @Suppress("UNCHECKED_CAST")
    override fun converter(): FromStringConverter<Set<T>> = fromStringConverter { it: String ->
        separator.split(it)
            .map { member -> fromStringConverter.convert(member) ?: error("Failed to convert $member") }
            .toSet() as Set<T>
    }

    override fun reverseConverter(): Converter<Set<T>, String> = object : ConverterBase<Set<T>, String>() {
        override fun onConvert(from: Set<T>): String =
            separator.join(from.map { toStringConverter.convert(it) ?: defaultToStringValue }
                .toSet())
    }
}

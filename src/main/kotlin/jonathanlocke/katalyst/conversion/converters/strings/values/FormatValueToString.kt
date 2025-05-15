package jonathanlocke.katalyst.conversion.converters.strings.values

import jonathanlocke.katalyst.conversion.converters.strings.ValueToStringConverter
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueTypeString
import jonathanlocke.katalyst.text.formatting.Formatter
import jonathanlocke.katalyst.text.formatting.formatters.anything.AnythingFormatters.Companion.convertToString

/**
 * Converts [From] -> [String] by using the given [Formatter]. The default formatter is [convertToString],
 * which simply calls [toString] on the value.
 *
 * **Properties**
 *
 * - [formatter] - The formatter to use to convert the value to a string
 *
 * @param From The type to convert from
 * @property formatter The formatter to use to convert the value to a string
 *
 * @see Formatter
 * @see ValueToStringConverter
 */
class FormatValueToString<From : Any>(
    override val from: ValueType<From>,
    val formatter: Formatter<From> = convertToString(),
) : ValueToStringConverter<From> {

    override val to: ValueType<String> = valueTypeString

    override fun convert(from: From?, problemHandler: ProblemHandler): String? =
        if (from != null) formatter.format(from) else "?"
}

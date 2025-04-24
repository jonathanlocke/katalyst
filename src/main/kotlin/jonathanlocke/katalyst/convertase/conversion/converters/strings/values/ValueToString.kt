package jonathanlocke.katalyst.convertase.conversion.converters.strings.values

import jonathanlocke.katalyst.convertase.conversion.converters.strings.ValueToStringConverter
import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import jonathanlocke.katalyst.cripsr.reflection.ValueClass.Companion.valueClass
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.formats.AnyFormatters.Companion.convertToString
import jonathanlocke.katalyst.nucleus.problems.ProblemListener

/**
 * Converts [From] -> [String] by using the given [StringFormatter]. The default formatter is [convertToString],
 * which simply calls [toString] on the value.
 *
 * **Properties**
 *
 * - [formatter] - The formatter to use to convert the value to a string
 *
 * @param From The type to convert from
 * @property formatter The formatter to use to convert the value to a string
 *
 * @see StringFormatter
 * @see ValueToStringConverter
 */
class ValueToString<From : Any>(
    override val from: ValueClass<From>,
    val formatter: StringFormatter<From> = convertToString()
) : ValueToStringConverter<From> {

    override val to: ValueClass<String> = valueClass(String::class)

    override fun convert(from: From?, listener: ProblemListener): String? =
        if (from != null) formatter.format(from) else "?"
}

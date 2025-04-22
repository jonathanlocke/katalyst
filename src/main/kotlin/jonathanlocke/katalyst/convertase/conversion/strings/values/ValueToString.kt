package jonathanlocke.katalyst.convertase.conversion.strings.values

import jonathanlocke.katalyst.convertase.conversion.strings.ValueToStringConverter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.formats.AnyFormatters.Companion.convertToString
import kotlin.reflect.KClass

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
    override val fromClass: KClass<From>,
    val formatter: StringFormatter<From> = convertToString()
) : ValueToStringConverter<From> {

    override val toClass: KClass<String> = String::class

    override fun convert(from: From?, listener: ProblemListener): String? =
        if (from != null) formatter.format(from) else "?"
}

package jonathanlocke.katalyst.convertase.strings.values

import jonathanlocke.katalyst.convertase.strings.ToStringConverter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.formats.Anything.Companion.ToString

/**
 * Converts [From] -> [String] by calling [Any.toString]
 */
class ToString<From : Any>(val formatter: StringFormatter<From> = ToString()) : ToStringConverter<From> {

    override fun convert(from: From?): String = if (from != null) formatter.format(from) else "?"
}

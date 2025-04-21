package jonathanlocke.katalyst.convertase.conversion.strings.values

import jonathanlocke.katalyst.convertase.conversion.strings.ValueToStringConverter
import jonathanlocke.katalyst.nucleus.language.functional.Reporter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.formats.Anything.Companion.convertToString
import kotlin.reflect.KClass

/**
 * Converts [From] -> [String] by calling [Any.toString]
 */
class ValueToString<From : Any>(
    override val fromClass: KClass<From>,
    val formatter: StringFormatter<From> = convertToString()
) :
    ValueToStringConverter<From> {

    override val toClass: KClass<String> = String::class

    override fun convert(from: From?, reporter: Reporter<String>): String? =
        if (from != null) formatter.format(from) else "?"
}

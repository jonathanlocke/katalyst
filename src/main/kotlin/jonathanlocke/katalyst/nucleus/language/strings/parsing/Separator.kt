package jonathanlocke.katalyst.nucleus.language.strings.parsing

class Separator(val parseSeparator: Regex = Regex(",\\*"), val joinSeparator: String = ", ") {

    fun split(text: String) = text.split(parseSeparator)
    fun join(text: Sequence<String>) = text.joinToString { joinSeparator }
}

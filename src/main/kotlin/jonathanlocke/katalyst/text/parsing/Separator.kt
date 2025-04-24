package jonathanlocke.katalyst.text.parsing

class Separator(val parseSeparator: Regex = Regex(",\\s*"), val joinSeparator: String = ", ") {

    constructor(parseSeparator: String, joinSeparator: String = parseSeparator) : this(
        Regex(parseSeparator),
        joinSeparator
    )

    companion object {

        val COLON_SEPARATOR = Separator(":")
        val SLASH_SEPARATOR = Separator("/")
    }

    fun split(text: String) = text.split(parseSeparator)
    fun join(text: Iterable<String>) = text.joinToString { joinSeparator }
}

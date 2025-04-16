package jonathanlocke.katalyst.nucleus.language.strings.parsing

interface StringParser<T> {
    fun parse(string: String): T
}
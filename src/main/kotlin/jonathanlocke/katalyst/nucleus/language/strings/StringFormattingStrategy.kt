package jonathanlocke.katalyst.nucleus.language.strings

fun interface StringFormattingStrategy<T> {
    fun format(value: T): String
}

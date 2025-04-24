package jonathanlocke.katalyst.nucleus.language.strings.formatting

fun interface Formatter<T> {
    fun format(value: T): String
}

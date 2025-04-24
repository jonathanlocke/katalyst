package jonathanlocke.katalyst.text.formatting

fun interface Formatter<T> {
    fun format(value: T): String
}

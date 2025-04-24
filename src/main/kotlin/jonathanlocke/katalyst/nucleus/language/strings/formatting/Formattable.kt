package jonathanlocke.katalyst.nucleus.language.strings.formatting

interface Formattable<T> {
    @Suppress("UNCHECKED_CAST")
    fun format(formatter: Formatter<T>): String = formatter.format(this as T)
}
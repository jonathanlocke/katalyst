package jonathanlocke.katalyst.text.formatting

interface Formattable<Value> {
    @Suppress("UNCHECKED_CAST")
    fun format(formatter: Formatter<Value>): String = formatter.format(this as Value)
}
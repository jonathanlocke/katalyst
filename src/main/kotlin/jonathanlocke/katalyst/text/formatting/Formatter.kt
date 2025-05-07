package jonathanlocke.katalyst.text.formatting

fun interface Formatter<Value> {
    fun format(value: Value): String
}

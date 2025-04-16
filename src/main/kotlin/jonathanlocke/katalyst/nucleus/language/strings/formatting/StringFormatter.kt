package jonathanlocke.katalyst.nucleus.language.strings.formatting

fun interface StringFormatter<T> {

    companion object {

        fun <T : Any> T.format(formattingStrategy: StringFormatter<T>): String =
            formattingStrategy.format(this)
    }

    fun format(value: T): String
}

package jonathanlocke.katalyst.text.formatting.formatters.columnar

import jonathanlocke.katalyst.text.formatting.Formatter
import jonathanlocke.katalyst.text.formatting.formatters.columnar.ColumnarFormatter.Justify.LEFT

abstract class ColumnarFormatter<Value> : Formatter<Value> {

    enum class Type {
        FIXED_WIDTH, VARIABLE_WIDTH
    }

    enum class Justify {
        LEFT, RIGHT
    }

    class Column(
        val name: String,
        var width: Int,
        val type: Type,
        val justify: Justify,
        val maxWidth: Int = Int.MAX_VALUE,
    ) {

        private fun justify(text: String): String =
            if (justify == LEFT) text.padEnd(width) else text.padStart(width)

        fun format(field: String): String =
            when (type) {
                Type.FIXED_WIDTH -> field.let { if (it.length > width) it.substring(0, width) else it }
                    .let { justify(it) }

                Type.VARIABLE_WIDTH -> field.let {
                    if (it.length > width) {
                        width = (it.length).coerceAtMost(maxWidth)
                    }
                    if (it.length > maxWidth) it.substring(0, maxWidth) else it
                }.let { justify(it) }
            }
    }

    abstract val columns: List<Column>
    abstract fun fields(value: Value): List<String>

    override fun format(value: Value): String {
        val fields = fields(value)
        require(fields.size == columns.size)
        return fields(value).mapIndexed { index, field ->
            columns[index].format(field)
        }.joinToString(" \u2502 ")
    }
}

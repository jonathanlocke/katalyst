package jonathanlocke.katalyst.logging.logs.text

import jonathanlocke.katalyst.text.formatting.Formatter

abstract class ColumnarFormatter<Value>(val columns: List<Column>) : Formatter<Value> {

    enum class Type {
        FIXED_WIDTH, VARIABLE_WIDTH
    }

    class Column(
        val name: String,
        var width: Int,
        val type: Type = Type.FIXED_WIDTH,
        val maxWidth: Int = width
    ) {
        fun format(field: String): String =
            when (type) {
                Type.FIXED_WIDTH -> field.let { if (it.length > width) it.substring(0, width) else it }.padEnd(width)
                Type.VARIABLE_WIDTH -> field.let {
                    if (it.length > width) {
                        width = (it.length).coerceAtMost(maxWidth)
                    }
                    if (it.length > maxWidth) it.substring(0, maxWidth) else it
                }.padEnd(width)
            }
    }

    override fun format(value: Value): String {
        val fields = fields(value)
        require(fields.size == columns.size)
        return fields(value).mapIndexed { index, field ->
            columns[index].format(field)
        }.joinToString(" | ")
    }

    abstract fun fields(value: Value): List<String>
}
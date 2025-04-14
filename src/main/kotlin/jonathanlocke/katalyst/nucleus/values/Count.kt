package jonathanlocke.katalyst.nucleus.values

import jonathanlocke.katalyst.nucleus.language.strings.StringFormattingStrategy
import jonathanlocke.katalyst.nucleus.language.strings.ToString

@JvmInline
value class Count private constructor(val value: Long) : Comparable<Count>, ToString<Count> {

    companion object {
        
        val ThousandsSeparated = StringFormattingStrategy<Count> { "%,d".format(it.value) }

        fun of(value: Long): Count = Count(value)
            .also { require(value >= 0) { "Count must be non-negative, was $value" } }
    }

    override operator fun compareTo(other: Count): Int = this.value.compareTo(other.value)

    override fun toString(strategy: StringFormattingStrategy<Count>): String = strategy.format(this)
    override fun toString(): String = value.toString()

    fun plus(other: Count): Count = of(this.value + other.value)
    operator fun plus(other: Number): Count = of(this.value + other.toLong())

    operator fun minus(other: Count): Count = of((this.value - other.value).coerceAtLeast(0))
    operator fun minus(other: Number): Count = of((this.value - other.toLong()).coerceAtLeast(0))

    operator fun times(other: Count): Count = of(this.value * other.value)
    operator fun times(other: Number): Count = of(this.value * other.toLong())

    operator fun inc(): Count = of(this.value + 1)
    operator fun dec(): Count = of((this.value - 1).coerceAtLeast(0))

    operator fun div(other: Count): Count = of(this.value / other.value)
        .also { require(other.value != 0L) { "Cannot divide by zero" } }

    operator fun div(other: Number): Count = of(this.value / other.toLong())
        .also { require(other.toLong() != 0L) { "Cannot divide by zero" } }

    operator fun rem(other: Count): Count = of(this.value % other.value)
        .also { require(other.value != 0L) { "Cannot mod by zero" } }

    operator fun rem(other: Number): Count = of(this.value % other.toLong())
        .also { require(other.toLong() != 0L) { "Cannot mod by zero" } }
}

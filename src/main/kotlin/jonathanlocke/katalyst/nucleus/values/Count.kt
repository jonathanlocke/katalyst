package jonathanlocke.katalyst.nucleus.values

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.handlers.Throw
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter

@JvmInline
value class Count private constructor(val value: Long) : Comparable<Count> {

    companion object {

        val ThousandsSeparated = StringFormatter<Count> { "%,d".format(it.value) }

        fun of(value: Long): Count = Count(value)
            .also { require(value >= 0) { "Count must be non-negative, was $value" } }

        fun parseCount(text: String): Count = parseCount(text)

        fun parseCount(
            text: String,
            error: ErrorHandler<Count> = Throw()
        ): Count? {

            val value = text.replace(",", "").toLongOrNull()
            return if (value == null) {
                error.error("Could not parse bytes: $text")
            } else {
                Count(value)
            }
        }
    }

    fun loop(code: () -> Unit) = (0 until value).forEach { code() }

    fun isZero(): Boolean = this.value == 0L

    fun asLong() = value
    fun asDouble() = value.toDouble()
    fun asFloat() = value.toFloat()
    fun asInt() = value.toInt()
    fun asShort() = value.toShort()
    fun asByte() = value.toByte()

    override operator fun compareTo(other: Count): Int = this.value.compareTo(other.value)

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

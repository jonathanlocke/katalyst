package jonathanlocke.katalyst.nucleus.values.count

import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.toValue
import jonathanlocke.katalyst.convertase.conversion.strings.values.StringToNumber.Companion.longConverter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter
import jonathanlocke.katalyst.nucleus.language.problems.reporters.Throw
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.ThousandsSeparated
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.count
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.parseCount

/**
 * Represents a positive integer count of something.
 *
 *  **Creation**
 *
 *  - [count]
 *  - [parseCount] - Parses text to a [Count], handling problems as specified by the given [ProblemReporter]
 *
 *  **Conversion**
 *
 *  - [asLong], [asDouble], [asFloat], [asInt], [asShort], [asByte]
 *
 *  **Formatting**
 *
 *  - [ThousandsSeparated]
 *
 *  **Language**
 *
 *  - [equals], [hashCode], [compareTo], [toString]
 *
 *  **Operators**
 *
 *  - [plus], [minus], [times], [div], [rem], [inc], [dec]
 *
 *  **Other**
 *
 *  - [isZero], [loop]
 *
 */
@JvmInline
value class Count private constructor(val value: Long) : Comparable<Count> {

    companion object {

        val ThousandsSeparated = StringFormatter<Count> { "%,d".format(it.value) }

        fun countConverter() = stringToValueConverter(Count::class) { text, reporter ->
            text.toValue(longConverter, Throw())?.toCount() ?: reporter.error("Could not parse count: $text")
        }

        fun Number.toCount(): Count = count(this.toLong())

        fun count(value: Long): Count =
            Count(value).also { require(value >= 0) { "Count must be non-negative, was $value" } }

        fun parseCount(text: String): Count = parseCount(text)

        fun parseCount(
            text: String, reporter: ProblemReporter<Count> = Throw()
        ): Count? {
            val value = text.replace(",", "").toLongOrNull()
            return if (value == null) {
                reporter.error("Could not parse bytes: $text", value = value)
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

    fun plus(other: Count): Count = count(this.value + other.value)
    operator fun plus(other: Number): Count = count(this.value + other.toLong())

    operator fun minus(other: Count): Count = count((this.value - other.value).coerceAtLeast(0))
    operator fun minus(other: Number): Count = count((this.value - other.toLong()).coerceAtLeast(0))

    operator fun times(other: Count): Count = count(this.value * other.value)
    operator fun times(other: Number): Count = count(this.value * other.toLong())

    operator fun inc(): Count = count(this.value + 1)
    operator fun dec(): Count = count((this.value - 1).coerceAtLeast(0))

    operator fun div(other: Count): Count =
        count(this.value / other.value).also { require(other.value != 0L) { "Cannot divide by zero" } }

    operator fun div(other: Number): Count =
        count(this.value / other.toLong()).also { require(other.toLong() != 0L) { "Cannot divide by zero" } }

    operator fun rem(other: Count): Count =
        count(this.value % other.value).also { require(other.value != 0L) { "Cannot mod by zero" } }

    operator fun rem(other: Number): Count =
        count(this.value % other.toLong()).also { require(other.toLong() != 0L) { "Cannot mod by zero" } }
}

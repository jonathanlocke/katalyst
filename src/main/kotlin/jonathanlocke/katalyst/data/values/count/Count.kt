package jonathanlocke.katalyst.data.values.count

import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.convert
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.conversion.converters.strings.values.StringToNumber.Companion.longConverter
import jonathanlocke.katalyst.data.values.count.Count.Companion.ThousandsSeparatedFormatter
import jonathanlocke.katalyst.data.values.count.Count.Companion.parseCount
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.Throw
import jonathanlocke.katalyst.reflection.ValueType.Companion.propertyClass
import jonathanlocke.katalyst.text.formatting.Formattable
import jonathanlocke.katalyst.text.formatting.Formatter

/**
 * Represents a positive integer count of something.
 *
 *  **Creation**
 *
 *  - [count]
 *  - [parseCount] - Parses text to a [Count], handling problems as specified by the given [ProblemListener]
 *
 *  **Conversion**
 *
 *  - [asLong], [asDouble], [asFloat], [asInt], [asShort], [asByte]
 *
 *  **Formatting**
 *
 *  - [ThousandsSeparatedFormatter]
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
value class Count private constructor(val count: Long) : Comparable<Count>, Formattable<Count> {

    companion object {

        val ThousandsSeparatedFormatter = Formatter<Count> { "%,d".format(it.count) }

        /**
         * Returns a converter that converts a string to a [Count], reporting any problems to the given listener.
         */
        fun countConverter() = stringToValueConverter(propertyClass(Count::class)) { text, listener ->
            text.convert(longConverter, Throw())?.toCount() ?: listener.error("Could not parse count: $text")
                .let { null }
        }

        /**
         * Converts the given number to a [Count].
         */
        fun Number.toCount(): Count = count(this.toLong(), Throw())!!

        /**
         * Converts the given number to a [Count].
         */
        fun Number.toCount(problemListener: ProblemListener = Throw()): Count? = count(this.toLong(), problemListener)

        /**
         * Creates a [Count] object with the given value.
         */
        fun count(value: Long): Count = count(value, Throw())!!

        /**
         * Creates a [Count] object with the given value.
         */
        fun count(value: Long, problemListener: ProblemListener = Throw()): Count? = if (value < 0) {
            problemListener.error("Count must be non-negative, was $value").let { null }
        } else {
            Count(value)
        }

        /**
         * Parses the given text into a [Count]
         */
        fun parseCount(text: String): Count = parseCount(text, Throw())!!

        /**
         * Parses the given text into a [Count], reporting any problems to the given listener.
         */
        fun parseCount(text: String, listener: ProblemListener = Throw()): Count? {
            val value = text.replace(",", "").toLongOrNull()
            return if (value == null) {
                listener.error("Could not parse bytes: $text", value = value).let { null }
            } else {
                Count(value)
            }
        }
    }

    fun max(other: Count): Count = count(this.count.coerceAtLeast(other.count))
    fun min(other: Count): Count = count(this.count.coerceAtMost(other.count))
    fun inRange(min: Count, max: Count): Count = count(this.count.coerceIn(min.count, max.count))
    fun loop(code: () -> Unit) = (0 until count).forEach { code() }
    fun isZero(): Boolean = this.count == 0L
    fun asLong() = count
    fun asDouble() = count.toDouble()
    fun asFloat() = count.toFloat()
    fun asInt() = count.toInt()
    fun asShort() = count.toShort()
    fun asByte() = count.toByte()

    override operator fun compareTo(other: Count): Int = this.count.compareTo(other.count)

    override fun toString(): String = count.toString()

    operator fun plus(other: Count): Count = count(this.count + other.count)
    operator fun plus(other: Number): Count = count(this.count + other.toLong())

    operator fun minus(other: Count): Count = count((this.count - other.count).coerceAtLeast(0))
    operator fun minus(other: Number): Count = count((this.count - other.toLong()).coerceAtLeast(0))

    operator fun times(other: Count): Count = count(this.count * other.count)
    operator fun times(other: Number): Count = count(this.count * other.toLong())

    operator fun inc(): Count = count(this.count + 1)
    operator fun dec(): Count = count((this.count - 1).coerceAtLeast(0))

    operator fun div(other: Count): Count =
        count(this.count / other.count).also { require(other.count != 0L) { "Cannot divide by zero" } }

    operator fun div(other: Number): Count =
        count(this.count / other.toLong()).also { require(other.toLong() != 0L) { "Cannot divide by zero" } }

    operator fun rem(other: Count): Count =
        count(this.count % other.count).also { require(other.count != 0L) { "Cannot mod by zero" } }

    operator fun rem(other: Number): Count =
        count(this.count % other.toLong()).also { require(other.toLong() != 0L) { "Cannot mod by zero" } }
}

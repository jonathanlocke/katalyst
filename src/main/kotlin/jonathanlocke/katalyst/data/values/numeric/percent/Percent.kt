package jonathanlocke.katalyst.data.values.numeric.percent

import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.data.values.numeric.Numeric
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.DecimalFormat
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.IntegerFormat
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.parsePercent
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.percent
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.percentConverter
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.ThrowOnError.Companion.throwOnError
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.text.formatting.Formattable
import jonathanlocke.katalyst.text.formatting.Formatter

/**
 * A percentage of any range (including percentages less than 0% and greater than 100%).
 *
 * **Creation**
 *
 *  - [percent] - Creates a percent object from a percentage value
 *  - [parsePercent] - Parses a text into a percent object, reporting any problems to the given listener
 *
 * **Comparison**
 *
 *  - [compareTo]
 *  - [equals]
 *  - [hashCode]
 *
 * **Conversion**
 *
 *  - [percentConverter]
 *  - [asUnitValue]
 *  - [asZeroToOne]
 *
 * **Operators**
 *
 *  - [minus]
 *  - [plus]
 *  - [times]
 *  - [div]
 *
 * **String Representation**
 *
 *  - [toString]
 *  - [IntegerFormat]
 *  - [DecimalFormat]
 *
 * @property percent The percentage value of this percent on the scale of 0 to 100, but potentially greater than 100 or less than 0
 */
class Percent(val percent: Double) : Comparable<Percent>, Formattable<Percent>, Numeric {

    /**
     * This percent as a unit value, potentially greater than 1 or less than 0 (if the percentage is greater than 100 or less than 0)
     *
     * @return This percentage divided by 100
     */
    fun asUnitValue(): Double = percent / 100.0

    /**
     * Returns this percentage scaled and coerced to the unit interval
     */
    fun asZeroToOne(): Double = asUnitValue().coerceIn(0.0, 1.0)

    fun isZero(): Boolean = percent == 0.0

    override fun asNumber(): Number = percent

    operator fun minus(that: Percent): Percent = Percent(percent - that.percent)
    operator fun minus(that: Number): Percent = Percent(percent - that.toDouble())
    operator fun minus(other: Numeric): Percent = minus(other.asNumber())
    operator fun plus(that: Percent): Percent = Percent(percent + that.percent)
    operator fun plus(that: Number): Percent = Percent(percent + that.toDouble())
    operator fun plus(other: Numeric): Percent = plus(other.asNumber())
    operator fun times(that: Percent): Percent = Percent(percent * that.percent / 100.0)
    operator fun times(that: Number): Percent = Percent(percent * that.toDouble())
    operator fun times(other: Numeric): Percent = times(other.asNumber())
    operator fun div(that: Percent): Percent = Percent(percent / that.percent * 100.0)
    operator fun div(that: Number): Percent = Percent(percent / that.toDouble())
    operator fun div(other: Numeric): Percent = div(other.asNumber())

    override fun hashCode(): Int = percent.hashCode()
    override fun equals(other: Any?): Boolean =
        (this === other) || (other is Percent && this.percent == other.percent)

    override fun compareTo(that: Percent): Int = this.percent.compareTo(that.percent)
    override fun toString(): String = format(IntegerFormat)

    fun min(that: Percent): Percent = if (this.percent <= that.percent) this else that
    fun max(that: Percent): Percent = if (this.percent >= that.percent) this else that

    fun inRange(min: Percent, max: Percent): Percent = percent(this.percent.coerceIn(min.percent, max.percent))

    companion object {

        /**
         * Returns a converter that converts a string to a [Percent], reporting any problems to the given listener.
         */
        fun percentConverter() =
            stringToValueConverter(valueType(Percent::class)) { text, listener -> parsePercent(text, listener) }

        val IntegerFormat = Formatter<Percent> { "${it.percent.toLong()}%" }
        val DecimalFormat = Formatter<Percent> { "%.1f%%".format(it.percent) }

        /**
         * Parses the given text into a Percent object and throws if that fails
         */
        fun parsePercent(text: String): Percent = parsePercent(text, throwOnError)!!

        /**
         * Parses the given text into a Percent object.
         *
         * @param text The text to parse
         * @param listener The problem listener to use if the text cannot be parsed as a percent
         * @return The percent
         */
        fun parsePercent(text: String, listener: ProblemListener = throwOnError): Percent? {
            return listener.guard("Could not parse percentage: $text") {
                val percentage = text.trim().trimEnd('%').toDouble()
                percent(percentage)
            }
        }

        /**
         * Creates a [Percent] object
         * @param percent The percentage (can be greater than 100% or less than 0%)
         * @return The percent
         */
        fun percent(percent: Number): Percent = Percent(percent.toDouble())

        /**
         * Creates a [Percent] object
         * @param percent The percentage (can be greater than 100% or less than 0%)
         * @return The percent
         */
        fun percent(percent: Numeric): Percent = percent(percent.asNumber())
    }
}
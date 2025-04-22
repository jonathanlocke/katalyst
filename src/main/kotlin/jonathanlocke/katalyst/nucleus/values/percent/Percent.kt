package jonathanlocke.katalyst.nucleus.values.percent

import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.nucleus.language.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.language.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter
import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter.Companion.format
import jonathanlocke.katalyst.nucleus.values.percent.Percent.Companion.DecimalFormat
import jonathanlocke.katalyst.nucleus.values.percent.Percent.Companion.IntegerFormat
import jonathanlocke.katalyst.nucleus.values.percent.Percent.Companion.parsePercent
import jonathanlocke.katalyst.nucleus.values.percent.Percent.Companion.percent
import jonathanlocke.katalyst.nucleus.values.percent.Percent.Companion.stringToPercentConverter

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
 *  - [stringToPercentConverter]
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
 * @property percentage The percentage value of this percent on the scale of 0 to 100, but potentially greater than 100 or less than 0
 */
class Percent(val percentage: Double) : Comparable<Percent> {

    /**
     * This percent as a unit value, potentially greater than 1 or less than 0 (if the percentage is greater than 100 or less than 0)
     *
     * @return This percentage divided by 100
     */
    fun asUnitValue(): Double = percentage / 100.0

    /**
     * Returns this percentage scaled and coerced to the unit interval
     */
    fun asZeroToOne(): Double = asUnitValue().coerceIn(0.0, 1.0)

    override fun compareTo(that: Percent): Int = this.percentage.compareTo(that.percentage)
    override fun toString(): String = this.format(DecimalFormat)
    operator fun minus(that: Percent): Percent = Percent(percentage - that.percentage)
    operator fun minus(that: Double): Percent = Percent(percentage - that)
    operator fun plus(that: Percent): Percent = Percent(percentage + that.percentage)
    operator fun plus(that: Double): Percent = Percent(percentage + that)
    operator fun times(that: Percent): Percent = Percent(percentage * that.percentage / 100.0)
    operator fun times(that: Double): Percent = Percent(percentage * that)
    operator fun div(that: Percent): Percent = Percent(percentage / that.percentage * 100.0)
    operator fun div(that: Double): Percent = Percent(percentage / that)
    override fun hashCode(): Int = percentage.hashCode()
    override fun equals(other: Any?): Boolean =
        (this === other) || (other is Percent && this.percentage == other.percentage)

    companion object {

        fun stringToPercentConverter() =
            stringToValueConverter(Percent::class) { text, listener -> parsePercent(text, listener) }

        val IntegerFormat = StringFormatter<Percent> { "${it.percentage.toLong()}%" }
        val DecimalFormat = StringFormatter<Percent> { "%.1f%%".format(it.percentage) }

        /**
         * Parses the given text into a Percent object.
         *
         * @param text The text to parse
         * @param listener The problem listener to use if the text cannot be parsed as a percent
         * @return The percent
         */
        fun parsePercent(text: String, listener: ProblemListener<Percent> = Throw()): Percent? {
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
    }
}
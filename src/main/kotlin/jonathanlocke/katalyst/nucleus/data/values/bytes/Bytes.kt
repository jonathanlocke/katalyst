package jonathanlocke.katalyst.nucleus.data.values.bytes

import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.cripsr.reflection.PropertyClass.Companion.valueClass
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.bytesConverter
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.exabytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.exbibytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.gibibytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.gigabytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.kibibytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.kilobytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.mebibytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.megabytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.parseBytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.pebibytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.petabytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.tebibytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.Companion.terabytes
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.MeasurementSystem.Binary
import jonathanlocke.katalyst.nucleus.data.values.bytes.Bytes.MeasurementSystem.Metric
import jonathanlocke.katalyst.nucleus.language.strings.formatting.Formattable
import jonathanlocke.katalyst.nucleus.language.strings.formatting.Formatter
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import java.text.DecimalFormat

/**
 * An immutable byte count, always positive, but possibly fractional (1.5 bytes is a valid value).
 *
 * Two [MeasurementSystem]s are supported: [Metric] and [Binary]. The metric system is the SI standard where
 * units are multiples of 1000, and the binary system is the IEC standard, where units are multiples of 1024.
 *
 * **Creation**
 *
 * Several factory methods are provided for creating [Bytes] objects in both measurement systems:
 *
 * *Metric System*
 *
 *  - [bytes], [kilobytes], [megabytes], [gigabytes], [terabytes], [petabytes], [exabytes]
 *
 * *Binary System*
 *
 *  - [kibibytes], [mebibytes], [gibibytes], [tebibytes], [pebibytes], [exbibytes]
 *
 * *Either System*
 *
 *  - [parseBytes] - Parses text to a [Bytes] value in the given [MeasurementSystem], handling problems as specified
 *                   by the given [ProblemReporter]
 *
 * **Conversion**
 *
 *  - [bytesConverter] - Returns a [StringToValueConverter] that converts a string to a [Bytes] value
 *  - [asBytes], [asKilobytes], [asMegabytes], [asGigabytes], [asTerabytes], [asPetabytes], [asExabytes]
 *  - [asKibibytes], [asMebibytes], [asGibibytes], [asTebibytes], [asPebibytes], [asExbibytes]
 *  - [asMetricString], [asBinaryString]
 *
 *  **Language**
 *
 *  - [compareTo], [equals], [hashCode], [toString]
 *
 * **Operators**
 *
 * - [plus], [div], [minus], [times]
 *
 * @property bytes The byte count
 * @see StringToValueConverter
 * @see ProblemListener
 */
class Bytes(val bytes: Double) : Formattable<Bytes> {

    enum class MeasurementSystem(val radix: Double, suffixPattern: String) {

        // SI units
        Metric(
            1000.0,
            """(?x) ( (?i: (?: |kilo|mega|giga|tera|peta|exa) byte(?: s)? ) | (?i: (?: K|M|G|T|P|X) (?: b|B)? ) )"""
        ),

        // IEC units
        Binary(
            1024.0,
            """(?x) ( (?i: (?: |kibi|mebi|gibi|tebi|pebi|exbi) byte(?:s)? ) | (?i: (?: K|M|G|T|P|X) (?: b|iB)? ) )"""
        );

        val pattern = Regex("""(?x) (?<value> \d+ (\.\d+)?) \s? (?<units> $suffixPattern)?""")
    }

    init {
        require(bytes >= 0) { "Byte count cannot be negative." }
    }

    companion object {

        val MetricFormatter = Formatter<Bytes> { it.asMetricString() }
        val BinaryFormatter = Formatter<Bytes> { it.asMetricString() }

        fun bytesConverter(measurementSystem: MeasurementSystem = Metric) =
            stringToValueConverter(valueClass(Bytes::class)) { text, listener ->
                parseBytes(text, measurementSystem, listener)
            }

        fun Number.toBytes() = bytes(this)
        fun bytes(value: Number) = Bytes(value.toDouble())

        fun kilobytes(value: Number) = bytes(value.toDouble() * Metric.radix)
        fun megabytes(value: Number) = kilobytes(value.toDouble() * Metric.radix)
        fun gigabytes(value: Number) = megabytes(value.toDouble() * Metric.radix)
        fun terabytes(value: Number) = gigabytes(value.toDouble() * Metric.radix)
        fun petabytes(value: Number) = terabytes(value.toDouble() * Metric.radix)
        fun exabytes(value: Number) = petabytes(value.toDouble() * Metric.radix)

        fun kibibytes(value: Number) = bytes(value.toDouble() * Binary.radix)
        fun mebibytes(value: Number) = kibibytes(value.toDouble() * Binary.radix)
        fun gibibytes(value: Number) = mebibytes(value.toDouble() * Binary.radix)
        fun tebibytes(value: Number) = gibibytes(value.toDouble() * Binary.radix)
        fun pebibytes(value: Number) = tebibytes(value.toDouble() * Binary.radix)
        fun exbibytes(value: Number) = pebibytes(value.toDouble() * Binary.radix)

        fun parseBytes(text: String, system: MeasurementSystem = Metric): Bytes = parseBytes(text, system, Throw())!!

        fun parseBytes(
            text: String, system: MeasurementSystem = Metric, listener: ProblemListener = Throw()
        ): Bytes? {

            val match = system.pattern.matchEntire(text)
            if (match != null) {

                val number = match.groups["value"]!!.value.replace(",", "").toDouble()
                val units = match.groups["units"]?.value?.lowercase()?.removeSuffix("s") ?: "byte"

                return when (system) {

                    Metric -> when (units.lowercase()) {
                        "byte" -> bytes(number)
                        "kilobyte", "k", "kb" -> kilobytes(number)
                        "megabyte", "m", "mb" -> megabytes(number)
                        "gigabyte", "g", "gb" -> gigabytes(number)
                        "terabyte", "t", "tb" -> terabytes(number)
                        "petabyte", "p", "pb" -> petabytes(number)
                        "exabyte", "x", "xb" -> exabytes(number)
                        else -> listener.error("Unsupported units format: $units").let { null }
                    }

                    Binary -> when (units) {
                        "byte" -> bytes(number)
                        "kibibyte", "k", "kb", "kib" -> kilobytes(number)
                        "mebibyte", "m", "mb", "mib" -> megabytes(number)
                        "gibibyte", "g", "gb", "gib" -> gigabytes(number)
                        "tebibyte", "t", "tb", "tib" -> terabytes(number)
                        "pebibyte", "p", "pb", "pib" -> petabytes(number)
                        "exbibyte", "x", "xb", "xib" -> exabytes(number)
                        else -> listener.error("Unsupported units format: $units").let { null }
                    }
                }
            }

            return listener.error("Could not parse bytes: $text").let { null }
        }
    }

    fun isZero() = bytes == 0.0

    override fun hashCode() = bytes.hashCode()
    override fun equals(other: Any?) = other is Bytes && other.bytes == bytes
    override fun toString() = asMetricString()

    operator fun plus(that: Bytes) = Bytes(asBytes() + that.asBytes())
    operator fun div(that: Bytes) = asBytes() / that.asBytes()
    operator fun div(that: Number) = Bytes(asBytes() / that.toDouble())
    operator fun minus(that: Bytes) = Bytes(asBytes() - that.asBytes())
    operator fun minus(that: Number) = Bytes(asBytes() - that.toDouble())
    operator fun times(that: Bytes) = Bytes(asBytes() * that.asBytes())
    operator fun times(that: Number) = Bytes(asBytes() * that.toDouble())
    operator fun compareTo(that: Bytes) = (asBytes() - that.asBytes()).toInt()
    operator fun compareTo(that: Number) = (asBytes() - that.toDouble()).toInt()
    operator fun plus(that: Number) = Bytes(asBytes() + that.toDouble())
    operator fun rem(that: Number) = Bytes(asBytes() % that.toDouble())

    operator fun inc(): Bytes = bytes(this.bytes + 1)
    operator fun dec(): Bytes = bytes(this.bytes - 1)

    fun max(other: Bytes): Bytes = bytes(this.bytes.coerceAtLeast(other.bytes))
    fun min(other: Bytes): Bytes = bytes(this.bytes.coerceAtMost(other.bytes))
    fun inRange(min: Bytes, max: Bytes): Bytes = bytes(this.bytes.coerceIn(min.bytes, max.bytes))

    fun asBytes() = this.bytes

    fun asKilobytes() = asBytes() / Metric.radix
    fun asMegabytes() = asKilobytes() / Metric.radix
    fun asGigabytes() = asMegabytes() / Metric.radix
    fun asTerabytes() = asGigabytes() / Metric.radix
    fun asPetabytes() = asTerabytes() / Metric.radix
    fun asExabytes() = asPetabytes() / Metric.radix

    fun asKibibytes() = asBytes() / Binary.radix
    fun asMebibytes() = asKibibytes() / Binary.radix
    fun asGibibytes() = asMebibytes() / Binary.radix
    fun asTebibytes() = asGibibytes() / Binary.radix
    fun asPebibytes() = asTebibytes() / Binary.radix
    fun asExbibytes() = asPebibytes() / Binary.radix

    fun asMetricString(): String = when {
        asExabytes() >= Metric.radix -> format(asPetabytes(), "Xb")
        asPetabytes() >= Metric.radix -> format(asGigabytes(), "Pb")
        asGigabytes() >= Metric.radix -> format(asTerabytes(), "Tb")
        asMegabytes() >= Metric.radix -> format(asGigabytes(), "Gb")
        asKilobytes() >= Metric.radix -> format(asMegabytes(), "Mb")
        bytes >= Metric.radix -> format(asKilobytes(), "Kb")
        else -> format(bytes, "bytes")
    }

    fun asBinaryString(): String = when {
        asExbibytes() >= Binary.radix -> format(asPebibytes(), "XiB")
        asPebibytes() >= Binary.radix -> format(asGibibytes(), "PiB")
        asGibibytes() >= Binary.radix -> format(asTebibytes(), "TiB")
        asMegabytes() >= Binary.radix -> format(asGibibytes(), "GiB")
        asKilobytes() >= Binary.radix -> format(asMebibytes(), "MiB")
        bytes >= Binary.radix -> format(asKibibytes(), "KiB")
        else -> format(bytes, "bytes")
    }

    private fun format(value: Double, units: String): String {

        val number = DecimalFormat("#.#").apply { isDecimalSeparatorAlwaysShown = false }.format(value)
        return "$number ${if (value == 1.0) units.removeSuffix("s") else units}"
    }
}

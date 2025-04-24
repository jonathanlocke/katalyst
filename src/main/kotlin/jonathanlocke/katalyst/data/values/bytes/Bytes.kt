package jonathanlocke.katalyst.data.values.bytes

import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.bytesConverter
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.exabytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.exbibytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.gibibytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.gigabytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.kibibytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.kilobytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.mebibytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.megabytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.parseBytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.pebibytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.petabytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.tebibytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.Companion.terabytes
import jonathanlocke.katalyst.data.values.bytes.Bytes.UnitSystem.IecUnits
import jonathanlocke.katalyst.data.values.bytes.Bytes.UnitSystem.SiUnits
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.Throw
import jonathanlocke.katalyst.reflection.ValueType.Companion.propertyClass
import jonathanlocke.katalyst.text.formatting.Formattable
import jonathanlocke.katalyst.text.formatting.Formatter
import java.text.DecimalFormat

/**
 * An immutable byte count, always positive, but possibly fractional (1.5 bytes is a valid value).
 *
 * Two [UnitSystem]s are supported: [SiUnits] and [IecUnits]. The metric system is the SI standard where
 * units are multiples of 1000, and the binary system is the IEC standard, where units are multiples of 1024.
 *
 * **Creation**
 *
 * Several factory methods are provided for creating [Bytes] objects in both measurement systems:
 *
 * *SI Units (Metric System)*
 *
 *  - [bytes], [kilobytes], [megabytes], [gigabytes], [terabytes], [petabytes], [exabytes]
 *
 * *IEC Units (Binary System)*
 *
 *  - [kibibytes], [mebibytes], [gibibytes], [tebibytes], [pebibytes], [exbibytes]
 *
 * *Either System*
 *
 *  - [parseBytes] - Parses text to a [Bytes] value in the given [UnitSystem], handling problems as specified
 *                   by the given [ProblemReporter]
 *
 * **Conversion**
 *
 *  - [bytesConverter] - Returns a converter that converts a string to a [Bytes] value
 *  - [asBytes], [asKilobytes], [asMegabytes], [asGigabytes], [asTerabytes], [asPetabytes], [asExabytes]
 *  - [asKibibytes], [asMebibytes], [asGibibytes], [asTebibytes], [asPebibytes], [asExbibytes]
 *  - [asSiUnitsString], [asIecUnitsString]
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
 * @see ProblemListener
 */
@Suppress("SpellCheckingInspection")
class Bytes(val bytes: Double) : Formattable<Bytes> {

    enum class UnitSystem(val radix: Double, suffixPattern: String) {

        // Powers of 10
        SiUnits(
            1000.0,
            """(?x) ( (?i: (?: |kilo|mega|giga|tera|peta|exa) byte(?: s)? ) | (?i: (?: K|M|G|T|P|X) (?: b|B)? ) )"""
        ),

        // Powers of 2
        IecUnits(
            1024.0,
            """(?x) ( (?i: (?: |kibi|mebi|gibi|tebi|pebi|exbi) byte(?:s)? ) | (?i: (?: K|M|G|T|P|X) (?: b|iB)? ) )"""
        );

        val pattern = Regex("""(?x) (?<value> \d+ (\.\d+)?) \s? (?<units> $suffixPattern)?""")
    }

    init {
        require(bytes >= 0) { "Byte count cannot be negative." }
    }

    companion object {

        val SiUnitsFormatter = Formatter<Bytes> { it.asSiUnitsString() }
        val IecUnitsFormatter = Formatter<Bytes> { it.asIecUnitsString() }

        fun bytesConverter(unitSystem: UnitSystem = SiUnits) =
            stringToValueConverter(propertyClass(Bytes::class)) { text, listener ->
                parseBytes(text, unitSystem, listener)
            }

        fun Number.toBytes() = bytes(this)
        fun bytes(value: Number) = Bytes(value.toDouble())

        fun kilobytes(value: Number) = bytes(value.toDouble() * SiUnits.radix)
        fun megabytes(value: Number) = kilobytes(value.toDouble() * SiUnits.radix)
        fun gigabytes(value: Number) = megabytes(value.toDouble() * SiUnits.radix)
        fun terabytes(value: Number) = gigabytes(value.toDouble() * SiUnits.radix)
        fun petabytes(value: Number) = terabytes(value.toDouble() * SiUnits.radix)
        fun exabytes(value: Number) = petabytes(value.toDouble() * SiUnits.radix)

        fun kibibytes(value: Number) = bytes(value.toDouble() * IecUnits.radix)
        fun mebibytes(value: Number) = kibibytes(value.toDouble() * IecUnits.radix)
        fun gibibytes(value: Number) = mebibytes(value.toDouble() * IecUnits.radix)
        fun tebibytes(value: Number) = gibibytes(value.toDouble() * IecUnits.radix)
        fun pebibytes(value: Number) = tebibytes(value.toDouble() * IecUnits.radix)
        fun exbibytes(value: Number) = pebibytes(value.toDouble() * IecUnits.radix)

        fun parseBytes(text: String, system: UnitSystem = SiUnits): Bytes = parseBytes(text, system, Throw())!!

        fun parseBytes(
            text: String, system: UnitSystem = SiUnits, listener: ProblemListener = Throw()
        ): Bytes? {

            val match = system.pattern.matchEntire(text)
            if (match != null) {

                val number = match.groups["value"]!!.value.replace(",", "").toDouble()
                val units = match.groups["units"]?.value?.lowercase()?.removeSuffix("s") ?: "byte"

                return when (system) {

                    SiUnits -> when (units.lowercase()) {
                        "byte" -> bytes(number)
                        "kilobyte", "k", "kb" -> kilobytes(number)
                        "megabyte", "m", "mb" -> megabytes(number)
                        "gigabyte", "g", "gb" -> gigabytes(number)
                        "terabyte", "t", "tb" -> terabytes(number)
                        "petabyte", "p", "pb" -> petabytes(number)
                        "exabyte", "x", "xb" -> exabytes(number)
                        else -> listener.error("Unsupported units format: $units").let { null }
                    }

                    IecUnits -> when (units) {
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
    override fun toString() = asSiUnitsString()

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

    fun asKilobytes() = asBytes() / SiUnits.radix
    fun asMegabytes() = asKilobytes() / SiUnits.radix
    fun asGigabytes() = asMegabytes() / SiUnits.radix
    fun asTerabytes() = asGigabytes() / SiUnits.radix
    fun asPetabytes() = asTerabytes() / SiUnits.radix
    fun asExabytes() = asPetabytes() / SiUnits.radix

    fun asKibibytes() = asBytes() / IecUnits.radix
    fun asMebibytes() = asKibibytes() / IecUnits.radix
    fun asGibibytes() = asMebibytes() / IecUnits.radix
    fun asTebibytes() = asGibibytes() / IecUnits.radix
    fun asPebibytes() = asTebibytes() / IecUnits.radix
    fun asExbibytes() = asPebibytes() / IecUnits.radix

    fun asSiUnitsString(): String = when {
        asExabytes() >= SiUnits.radix -> format(asPetabytes(), "XB")
        asPetabytes() >= SiUnits.radix -> format(asGigabytes(), "PB")
        asGigabytes() >= SiUnits.radix -> format(asTerabytes(), "TB")
        asMegabytes() >= SiUnits.radix -> format(asGigabytes(), "GB")
        asKilobytes() >= SiUnits.radix -> format(asMegabytes(), "MB")
        bytes >= SiUnits.radix -> format(asKilobytes(), "KB")
        else -> format(bytes, " bytes")
    }

    fun asIecUnitsString(): String = when {
        asExbibytes() >= IecUnits.radix -> format(asPebibytes(), "XiB")
        asPebibytes() >= IecUnits.radix -> format(asGibibytes(), "PiB")
        asGibibytes() >= IecUnits.radix -> format(asTebibytes(), "TiB")
        asMegabytes() >= IecUnits.radix -> format(asGibibytes(), "GiB")
        asKilobytes() >= IecUnits.radix -> format(asMebibytes(), "MiB")
        bytes >= IecUnits.radix -> format(asKibibytes(), "KiB")
        else -> format(bytes, " bytes")
    }

    private fun format(value: Double, units: String): String {
        val number = DecimalFormat("#.#").apply { isDecimalSeparatorAlwaysShown = false }.format(value)
        return "$number${if (value == 1.0) units.removeSuffix("s") else units}"
    }
}

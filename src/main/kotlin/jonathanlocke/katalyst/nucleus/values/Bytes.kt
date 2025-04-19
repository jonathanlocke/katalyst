package jonathanlocke.katalyst.nucleus.values

import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.handlers.Throw
import jonathanlocke.katalyst.nucleus.values.Bytes.MeasurementSystem.Binary
import jonathanlocke.katalyst.nucleus.values.Bytes.MeasurementSystem.Metric
import java.text.DecimalFormat

class Bytes(val bytes: Double) {

    enum class MeasurementSystem(val radix: Double, suffixPattern: String) {

        // SI units
        Metric(1000.0, "((|kilo|mega|giga|tera|peta|exa)byte(s)?)(K|M|G|T|P|X)(|b|B)"),

        // IEC units
        Binary(1024.0, "((|kibi|mebi|gibi|tebi|pebi|exbi)byte(s)?)(K|M|G|T|P|X)(|B|iB)");

        val pattern = Regex("""(?x) (?<value> \d+ (\.\d+)?) \s? (?<units> $suffixPattern)""")
    }

    init {
        require(bytes >= 0) { "Byte count cannot be negative." }
    }

    companion object {

        fun bytesConverter(measurementSystem: MeasurementSystem = Metric) =
            stringToValueConverter(Bytes::class) { text, errorHandler ->
                parseBytes(text, measurementSystem, errorHandler)
            }

        fun bytes(value: Double) = Bytes(value)

        fun kilobytes(value: Double) = bytes(value * Metric.radix)
        fun megabytes(value: Double) = kilobytes(value * Metric.radix)
        fun gigabytes(value: Double) = megabytes(value * Metric.radix)
        fun terabytes(value: Double) = gigabytes(value * Metric.radix)
        fun petabytes(value: Double) = terabytes(value * Metric.radix)
        fun exabytes(value: Double) = petabytes(value * Metric.radix)

        fun kibibytes(value: Double) = bytes(value * Binary.radix)
        fun mebibytes(value: Double) = kibibytes(value * Binary.radix)
        fun gibibytes(value: Double) = mebibytes(value * Binary.radix)
        fun tebibytes(value: Double) = gibibytes(value * Binary.radix)
        fun pebibytes(value: Double) = tebibytes(value * Binary.radix)
        fun exbibytes(value: Double) = pebibytes(value * Binary.radix)

        fun parseBytes(text: String, system: MeasurementSystem = Metric): Bytes = parseBytes(text, system)

        fun parseBytes(
            text: String,
            system: MeasurementSystem = Metric,
            error: ErrorHandler<Bytes?> = Throw()
        ): Bytes? {

            val match = system.pattern.matchEntire(text)
            if (match != null) {

                val number = match.groups["value"]!!.value.replace(",", "").toDouble()
                val units = match.groups["units"]!!.value.lowercase().removeSuffix("s")

                return when (system) {

                    Metric -> when (units) {
                        "byte", "" -> bytes(number)
                        "kilobyte", "K", "Kb", "KB" -> kilobytes(number)
                        "megabyte", "M", "Mb", "MB" -> megabytes(number)
                        "gigabyte", "G", "Gb", "GB" -> gigabytes(number)
                        "terabyte", "T", "Tb", "TB" -> terabytes(number)
                        "petabyte", "P", "Pb", "PB" -> petabytes(number)
                        "exabyte", "X", "Xb", "XB" -> exabytes(number)
                        else -> error.error("Unsupported units format: $units")
                    }

                    Binary -> when (units) {
                        "byte", "" -> bytes(number)
                        "kibibyte", "K", "KB", "KiB" -> kilobytes(number)
                        "mebibyte", "M", "MB", "MiB" -> megabytes(number)
                        "gibibyte", "G", "GB", "GiB" -> gigabytes(number)
                        "tebibyte", "T", "TB", "TiB" -> terabytes(number)
                        "pebibyte", "P", "PB", "PiB" -> petabytes(number)
                        "exbibyte", "X", "XB", "XiB" -> exabytes(number)
                        else -> error.error("Unsupported units format: $units")
                    }
                }
            }

            return error.error("Could not parse bytes: $text")
        }
    }

    val isZero = bytes == 0.0

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
        else -> "$bytes bytes"
    }

    fun asBinaryString(): String = when {
        asExbibytes() >= Binary.radix -> format(asPebibytes(), "XiB")
        asPebibytes() >= Binary.radix -> format(asGibibytes(), "PiB")
        asGibibytes() >= Binary.radix -> format(asTebibytes(), "TiB")
        asMegabytes() >= Binary.radix -> format(asGibibytes(), "GiB")
        asKilobytes() >= Binary.radix -> format(asMebibytes(), "MiB")
        bytes >= Binary.radix -> format(asKibibytes(), "KiB")
        else -> "$bytes bytes"
    }

    private fun format(value: Double, units: String): String = DecimalFormat("#.##").format(value) + units
}

package jonathanlocke.katalyst.convertase.conversion.strings.values

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry.Companion.registerConversions
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.stringToValueConverter

/**
 * Converts [Number] -> [Int] for different kinds of Numbers
 */
class StringToNumber {

    init {
        registerConversions(this::class)
    }

    companion object {

        val byteConverter = stringToValueConverter(Byte::class) { text, reporter ->
            text.toByteOrNull() ?: reporter.error("Invalid Byte value $text")
        }

        val intConverter = stringToValueConverter(Int::class) { text, reporter ->
            text.toIntOrNull() ?: reporter.error("Invalid Int value $text")
        }

        val longConverter = stringToValueConverter(Long::class) { text, reporter ->
            text.toLongOrNull() ?: reporter.error("Invalid Long value $text")
        }

        val floatConverter = stringToValueConverter(Float::class) { text, reporter ->
            text.toFloatOrNull() ?: reporter.error("Invalid Float value $text")
        }

        val doubleConverter = stringToValueConverter(Double::class) { text, reporter ->
            text.toDoubleOrNull() ?: reporter.error("Invalid Double value $text")
        }

        val shortConverter = stringToValueConverter(Short::class) { text, reporter ->
            text.toShortOrNull() ?: reporter.error("Invalid Short value $text")
        }
    }
}

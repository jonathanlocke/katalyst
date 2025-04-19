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

        val byteConverter = stringToValueConverter(Byte::class) { text, errorHandler ->
            text.toByteOrNull() ?: errorHandler.error("Invalid Byte value $text")
        }

        val intConverter = stringToValueConverter(Int::class) { text, errorHandler ->
            text.toIntOrNull() ?: errorHandler.error("Invalid Int value $text")
        }

        val longConverter = stringToValueConverter(Long::class) { text, errorHandler ->
            text.toLongOrNull() ?: errorHandler.error("Invalid Long value $text")
        }

        val floatConverter = stringToValueConverter(Float::class) { text, errorHandler ->
            text.toFloatOrNull() ?: errorHandler.error("Invalid Float value $text")
        }

        val doubleConverter = stringToValueConverter(Double::class) { text, errorHandler ->
            text.toDoubleOrNull() ?: errorHandler.error("Invalid Double value $text")
        }

        val shortConverter = stringToValueConverter(Short::class) { text, errorHandler ->
            text.toShortOrNull() ?: errorHandler.error("Invalid Short value $text")
        }
    }
}

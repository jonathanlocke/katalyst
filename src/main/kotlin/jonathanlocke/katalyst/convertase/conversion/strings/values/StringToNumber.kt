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

        val byteConverter = stringToValueConverter(Byte::class) { text, errorBehavior ->
            text.toByteOrNull() ?: errorBehavior.error("Invalid Byte value $text")
        }

        val intConverter = stringToValueConverter(Int::class) { text, errorBehavior ->
            text.toIntOrNull() ?: errorBehavior.error("Invalid Int value $text")
        }

        val longConverter = stringToValueConverter(Long::class) { text, errorBehavior ->
            text.toLongOrNull() ?: errorBehavior.error("Invalid Long value $text")
        }

        val floatConverter = stringToValueConverter(Float::class) { text, errorBehavior ->
            text.toFloatOrNull() ?: errorBehavior.error("Invalid Float value $text")
        }

        val doubleConverter = stringToValueConverter(Double::class) { text, errorBehavior ->
            text.toDoubleOrNull() ?: errorBehavior.error("Invalid Double value $text")
        }

        val shortConverter = stringToValueConverter(Short::class) { text, errorBehavior ->
            text.toShortOrNull() ?: errorBehavior.error("Invalid Short value $text")
        }
    }
}

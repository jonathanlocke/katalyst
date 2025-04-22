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

        val byteConverter = stringToValueConverter(Byte::class) { text, listener ->
            text.toByteOrNull() ?: listener.error("Invalid Byte value $text").let { null }
        }

        val intConverter = stringToValueConverter(Int::class) { text, listener ->
            text.toIntOrNull() ?: listener.error("Invalid Int value $text").let { null }
        }

        val longConverter = stringToValueConverter(Long::class) { text, listener ->
            text.toLongOrNull() ?: listener.error("Invalid Long value $text").let { null }
        }

        val floatConverter = stringToValueConverter(Float::class) { text, listener ->
            text.toFloatOrNull() ?: listener.error("Invalid Float value $text").let { null }
        }

        val doubleConverter = stringToValueConverter(Double::class) { text, listener ->
            text.toDoubleOrNull() ?: listener.error("Invalid Double value $text").let { null }
        }

        val shortConverter = stringToValueConverter(Short::class) { text, listener ->
            text.toShortOrNull() ?: listener.error("Invalid Short value $text").let { null }
        }
    }
}
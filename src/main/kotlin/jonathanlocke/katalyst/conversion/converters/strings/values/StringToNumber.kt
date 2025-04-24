package jonathanlocke.katalyst.conversion.converters.strings.values

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.reflection.ValueType.Companion.propertyClass

/**
 * Converts [Number] -> [Int] for different kinds of Numbers
 */
class StringToNumber {

    companion object {

        fun registerConversions(conversionRegistry: ConversionRegistry) {
            conversionRegistry.registerAllConversions(this)
        }

        val byteConverter = stringToValueConverter(propertyClass(Byte::class)) { text, listener ->
            text.toByteOrNull() ?: listener.error("Invalid Byte value $text").let { null }
        }

        val intConverter = stringToValueConverter(propertyClass(Int::class)) { text, listener ->
            text.toIntOrNull() ?: listener.error("Invalid Int value $text").let { null }
        }

        val longConverter = stringToValueConverter(propertyClass(Long::class)) { text, listener ->
            text.toLongOrNull() ?: listener.error("Invalid Long value $text").let { null }
        }

        val floatConverter = stringToValueConverter(propertyClass(Float::class)) { text, listener ->
            text.toFloatOrNull() ?: listener.error("Invalid Float value $text").let { null }
        }

        val doubleConverter = stringToValueConverter(propertyClass(Double::class)) { text, listener ->
            text.toDoubleOrNull() ?: listener.error("Invalid Double value $text").let { null }
        }

        val shortConverter = stringToValueConverter(propertyClass(Short::class)) { text, listener ->
            text.toShortOrNull() ?: listener.error("Invalid Short value $text").let { null }
        }
    }
}
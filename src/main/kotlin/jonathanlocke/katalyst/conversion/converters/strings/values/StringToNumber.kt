package jonathanlocke.katalyst.conversion.converters.strings.values

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.strings.StringConversions.Companion.stringToValueConverter
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

/**
 * Converts [Number] -> [Int] for different kinds of Numbers
 */
class StringToNumber {

    companion object {

        fun registerConversions(conversionRegistry: ConversionRegistry) {
            conversionRegistry.registerAllConversions(this)
        }

        val byteConverter = stringToValueConverter(valueType(Byte::class)) { text, statusHandler ->
            text.toByteOrNull() ?: statusHandler.error("Invalid Byte value $text").let { null }
        }

        val intConverter = stringToValueConverter(valueType(Int::class)) { text, statusHandler ->
            text.toIntOrNull() ?: statusHandler.error("Invalid Int value $text").let { null }
        }

        val longConverter = stringToValueConverter(valueType(Long::class)) { text, statusHandler ->
            text.toLongOrNull() ?: statusHandler.error("Invalid Long value $text").let { null }
        }

        val floatConverter = stringToValueConverter(valueType(Float::class)) { text, statusHandler ->
            text.toFloatOrNull() ?: statusHandler.error("Invalid Float value $text").let { null }
        }

        val doubleConverter = stringToValueConverter(valueType(Double::class)) { text, statusHandler ->
            text.toDoubleOrNull() ?: statusHandler.error("Invalid Double value $text").let { null }
        }

        val shortConverter = stringToValueConverter(valueType(Short::class)) { text, statusHandler ->
            text.toShortOrNull() ?: statusHandler.error("Invalid Short value $text").let { null }
        }
    }
}
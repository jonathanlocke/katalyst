package jonathanlocke.katalyst.conversion.converters.strings.values

import jonathanlocke.katalyst.conversion.ConversionRegistry
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType

/**
 * Converts [Number] -> [Int] for different kinds of Numbers
 */
class StringToNumber {

    companion object {

        fun registerConversions(conversionRegistry: ConversionRegistry) {
            conversionRegistry.registerAllConversions(this)
        }

        val byteConverter = stringToValueConverter(valueType(Byte::class)) { text, problemHandler ->
            text.toByteOrNull() ?: problemHandler.error("Invalid Byte value $text").let { null }
        }

        val intConverter = stringToValueConverter(valueType(Int::class)) { text, problemHandler ->
            text.toIntOrNull() ?: problemHandler.error("Invalid Int value $text").let { null }
        }

        val longConverter = stringToValueConverter(valueType(Long::class)) { text, problemHandler ->
            text.toLongOrNull() ?: problemHandler.error("Invalid Long value $text").let { null }
        }

        val floatConverter = stringToValueConverter(valueType(Float::class)) { text, problemHandler ->
            text.toFloatOrNull() ?: problemHandler.error("Invalid Float value $text").let { null }
        }

        val doubleConverter = stringToValueConverter(valueType(Double::class)) { text, problemHandler ->
            text.toDoubleOrNull() ?: problemHandler.error("Invalid Double value $text").let { null }
        }

        val shortConverter = stringToValueConverter(valueType(Short::class)) { text, problemHandler ->
            text.toShortOrNull() ?: problemHandler.error("Invalid Short value $text").let { null }
        }
    }
}
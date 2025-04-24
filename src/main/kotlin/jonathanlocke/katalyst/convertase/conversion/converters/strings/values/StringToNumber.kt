package jonathanlocke.katalyst.convertase.conversion.converters.strings.values

import jonathanlocke.katalyst.convertase.conversion.ConversionRegistry
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.cripsr.reflection.ValueClass.Companion.valueClass

/**
 * Converts [Number] -> [Int] for different kinds of Numbers
 */
class StringToNumber {

    companion object {

        fun registerConversions(conversionRegistry: ConversionRegistry) {
            conversionRegistry.registerAllConversions(this)
        }

        val byteConverter = stringToValueConverter(valueClass(Byte::class)) { text, listener ->
            text.toByteOrNull() ?: listener.error("Invalid Byte value $text").let { null }
        }

        val intConverter = stringToValueConverter(valueClass(Int::class)) { text, listener ->
            text.toIntOrNull() ?: listener.error("Invalid Int value $text").let { null }
        }

        val longConverter = stringToValueConverter(valueClass(Long::class)) { text, listener ->
            text.toLongOrNull() ?: listener.error("Invalid Long value $text").let { null }
        }

        val floatConverter = stringToValueConverter(valueClass(Float::class)) { text, listener ->
            text.toFloatOrNull() ?: listener.error("Invalid Float value $text").let { null }
        }

        val doubleConverter = stringToValueConverter(valueClass(Double::class)) { text, listener ->
            text.toDoubleOrNull() ?: listener.error("Invalid Double value $text").let { null }
        }

        val shortConverter = stringToValueConverter(valueClass(Short::class)) { text, listener ->
            text.toShortOrNull() ?: listener.error("Invalid Short value $text").let { null }
        }
    }
}
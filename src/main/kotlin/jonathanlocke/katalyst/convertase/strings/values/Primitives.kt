package jonathanlocke.katalyst.convertase.strings.values

import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.fromStringConverter

/**
 * Converts [String] -> [Int]
 */
class Primitives {

    companion object {

        val ToByte = fromStringConverter { it.toByteOrNull() ?: error("Invalid Byte value $it") }
        val ToInt = fromStringConverter { it.toIntOrNull() ?: error("Invalid Int value $it") }
        val ToLong = fromStringConverter { it.toLongOrNull() ?: error("Invalid Long value $it") }
        val ToFloat = fromStringConverter { it.toFloatOrNull() ?: error("Invalid Float value $it") }
        val ToDouble = fromStringConverter { it.toDoubleOrNull() ?: error("Invalid Double value $it") }
        val ToShort = fromStringConverter { it.toShortOrNull() ?: error("Invalid Short value $it") }
    }
}

package jonathanlocke.katalyst.convertase.conversion.strings

import jonathanlocke.katalyst.convertase.conversion.Converter

/**
 * Converter from [Value] -> [String]
 */
interface ValueToStringConverter<Value : Any> : Converter<Value, String>

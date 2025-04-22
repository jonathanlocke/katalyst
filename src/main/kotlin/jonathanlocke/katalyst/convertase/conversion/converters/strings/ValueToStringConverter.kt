package jonathanlocke.katalyst.convertase.conversion.converters.strings

import jonathanlocke.katalyst.convertase.conversion.converters.Converter

/**
 * Converter from [Value] -> [String]
 */
interface ValueToStringConverter<Value : Any> : Converter<Value, String>

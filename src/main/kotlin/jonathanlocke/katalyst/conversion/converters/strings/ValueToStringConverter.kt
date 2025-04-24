package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.converters.Converter

/**
 * Converter from [Value] -> [String]
 */
interface ValueToStringConverter<Value : Any> : Converter<Value, String>

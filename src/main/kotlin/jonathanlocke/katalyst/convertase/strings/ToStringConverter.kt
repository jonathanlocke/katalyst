package jonathanlocke.katalyst.convertase.strings

import jonathanlocke.katalyst.convertase.Converter

/**
 * Converter from type [From] to a string value
 */
interface ToStringConverter<From : Any> : Converter<From, String>

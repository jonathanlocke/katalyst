package jonathanlocke.katalyst.convertase.strings

import jonathanlocke.katalyst.convertase.Converter

interface ToStringConverter<T : Any> : Converter<T, String>

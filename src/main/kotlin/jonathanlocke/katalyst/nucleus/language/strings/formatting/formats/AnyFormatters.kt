package jonathanlocke.katalyst.nucleus.language.strings.formatting.formats

import jonathanlocke.katalyst.nucleus.language.strings.formatting.Formatter

class AnyFormatters {

    companion object {

        /**
         * String formatter that produces a string value of any object by calling [toString].
         */
        fun <T> convertToString() = Formatter<T> { it.toString() }
    }
}

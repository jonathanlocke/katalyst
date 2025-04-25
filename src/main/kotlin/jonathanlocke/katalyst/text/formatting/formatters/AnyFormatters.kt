package jonathanlocke.katalyst.text.formatting.formatters

import jonathanlocke.katalyst.text.formatting.Formatter

class AnyFormatters {

    companion object {

        /**
         * String formatter that produces a string value of any object by calling [toString].
         */
        fun <T> convertToString() = Formatter<T> { it.toString() }
    }
}

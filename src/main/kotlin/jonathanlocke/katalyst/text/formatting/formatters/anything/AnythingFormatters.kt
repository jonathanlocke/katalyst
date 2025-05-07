package jonathanlocke.katalyst.text.formatting.formatters.anything

import jonathanlocke.katalyst.text.formatting.Formatter

class AnythingFormatters {

    companion object {

        /**
         * String formatter that produces a string value of any object by calling [toString].
         */
        fun <Value> convertToString() = Formatter<Value> { it.toString() }
    }
}
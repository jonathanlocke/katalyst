package jonathanlocke.katalyst.text.formatting.formatters.anything

import jonathanlocke.katalyst.text.formatting.Formatter

object AnythingFormatters {

    /**
     * String formatter that produces a string value of any object by calling [toString].
     */
    fun <Value> convertToString() = Formatter<Value> { it.toString() }
}

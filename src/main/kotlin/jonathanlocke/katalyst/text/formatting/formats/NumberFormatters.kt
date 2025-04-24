package jonathanlocke.katalyst.text.formatting.formats

import jonathanlocke.katalyst.text.formatting.Formatter

class NumberFormatters {

    companion object {
        val ThousandsSeparatedFormat = Formatter<Number> { "%,d".format(it.toLong()) }
    }
}
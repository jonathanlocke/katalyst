package jonathanlocke.katalyst.nucleus.language.strings.formatting.formats

import jonathanlocke.katalyst.nucleus.language.strings.formatting.Formatter

class NumberFormatters {

    companion object {
        val ThousandsSeparatedFormat = Formatter<Number> { "%,d".format(it.toLong()) }
    }
}
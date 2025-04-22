package jonathanlocke.katalyst.nucleus.language.strings.formatting.formats

import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter

class NumberFormatters {

    companion object {
        val ThousandsSeparatedFormat = StringFormatter<Number> { "%,d".format(it.toLong()) }
    }
}
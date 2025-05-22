package jonathanlocke.katalyst.text.formatting.formatters.numeric

import jonathanlocke.katalyst.text.formatting.Formatter

object NumberFormatters {

    val ThousandsSeparatedFormat = Formatter<Number> { "%,d".format(it.toLong()) }
}

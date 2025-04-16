package jonathanlocke.katalyst.convertase.strings.values

import jonathanlocke.katalyst.convertase.strings.FromStringConverter.Companion.convert
import jonathanlocke.katalyst.convertase.strings.FromStringConverterBase

class ToInt : FromStringConverterBase<Int>() {

    companion object {

        val CommaSeparated = FromStringConverterBase { text -> text.replace(",", "").convert(ToInt()) }
    }

    override fun onToValue(text: String): Int? = text.toIntOrNull() ?: error("Invalid integer value: $text")
}

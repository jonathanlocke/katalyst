package jonathanlocke.katalyst.convertase.strings.collections

import jonathanlocke.katalyst.convertase.ConversionBase
import jonathanlocke.katalyst.convertase.Converter
import jonathanlocke.katalyst.convertase.ConverterBase
import jonathanlocke.katalyst.convertase.strings.FromStringConverter
import jonathanlocke.katalyst.convertase.strings.FromStringConverterBase
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator

class ListConversion<T : Any>(
    private val toConverter: FromStringConverter<T>,
    private val fromConverter: Converter<T, String>,
    private val separator: Separator = Separator()
) : ConversionBase<String, List<T>>() {

    override fun to(): Converter<String, List<T>> = FromStringConverterBase {
        separator.split(it)
            .map { element -> toConverter.convert(element) ?: error("Failed to convert $element") }
            .toList()
    }

    override fun from(): Converter<List<T>, String> = object : ConverterBase<List<T>, String>() {
        override fun onConvert(from: List<T>): String =
            separator.join(from.map { it.toString() }.toList())
    }
}

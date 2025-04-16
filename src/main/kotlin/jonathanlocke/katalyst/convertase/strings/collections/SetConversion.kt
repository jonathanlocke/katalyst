package jonathanlocke.katalyst.convertase.strings.collections

import jonathanlocke.katalyst.convertase.ConversionBase
import jonathanlocke.katalyst.convertase.Converter
import jonathanlocke.katalyst.convertase.ConverterBase
import jonathanlocke.katalyst.convertase.strings.FromStringConverter
import jonathanlocke.katalyst.convertase.strings.FromStringConverterBase
import jonathanlocke.katalyst.convertase.strings.ToStringConverterBase

class SetConversion<T : Any>(
    private val fromConverter: FromStringConverter<T>,
    private val toConverter: ToStringConverterBase<T>,
    private val separator: String = ", "
) : ConversionBase<String, Set<T>>() {

    override fun to(): Converter<String, Set<T>> = FromStringConverterBase {
        it.split(Regex("$separator\\s*"))
            .map { element -> fromConverter.convert(element) ?: error("Failed to convert $element") }
            .toSet()
    }

    override fun from(): Converter<Set<T>, String> = object : ConverterBase<Set<T>, String>() {
        override fun onConvert(from: Set<T>): String = from.joinToString(separator) { it.toString() }
    }
}

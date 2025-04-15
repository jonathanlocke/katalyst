package jonathanlocke.katalyst.convertase.collections

import jonathanlocke.katalyst.convertase.StringConverter
import jonathanlocke.katalyst.convertase.TwoWayConverterBase
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class ListConverter<T : Any>(
    private val converter: StringConverter<T>,
    private val separator: String = ", "
) : TwoWayConverterBase<String, List<T>>(converter.fromType, List::class as KClass<List<T>>) {

    override fun onConvert(from: String): List<T> =
        from.split(Regex("$separator\\s*"))
            .map { converter.convert(it) ?: error("Failed to convert $it") }

    override fun onUnconvert(value: List<T>): String =
        value.joinToString(separator) { converter.unconvert(it).orEmpty() }

    override val fromType: KClass<String> = converter.fromType

    override val toType: KClass<List<T>> = List::class as KClass<List<T>>
}

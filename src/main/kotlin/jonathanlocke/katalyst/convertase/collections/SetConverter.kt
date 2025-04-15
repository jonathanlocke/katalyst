package jonathanlocke.katalyst.convertase.collections

import jonathanlocke.katalyst.convertase.StringConverter
import jonathanlocke.katalyst.convertase.TwoWayConverterBase
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class SetConverter<T : Any>(
    private val converter: StringConverter<T>,
    private val separator: String = ", ",
) : TwoWayConverterBase<String, Set<T>>(converter.fromType, Set::class as KClass<Set<T>>) {

    override fun onConvert(from: String): Set<T> =
        from.split(Regex("$separator\\s*"))
            .map { converter.convert(it) ?: error("Failed to convert $it") }
            .toSet()

    override fun onUnconvert(value: Set<T>): String =
        value.joinToString(separator) { converter.unconvert(it).orEmpty() }

    override val fromType: KClass<String> = converter.fromType

    override val toType: KClass<Set<T>> = Set::class as KClass<Set<T>>
}

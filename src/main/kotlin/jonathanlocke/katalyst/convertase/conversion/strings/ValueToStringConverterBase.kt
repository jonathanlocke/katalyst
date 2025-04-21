package jonathanlocke.katalyst.convertase.conversion.strings

import jonathanlocke.katalyst.convertase.conversion.ConverterBase
import kotlin.reflect.KClass

/**
 * Base class for implementing converters from [Value] -> [String].
 *
 * - [onToString] - Called with an always non-null value to convert to a string
 *
 * @param Value The type to convert from
 * @see ConverterBase
 * @see ValueToStringConverter
 */
abstract class ValueToStringConverterBase<Value : Any>(valueClass: KClass<Value>) :
    ConverterBase<Value, String>(valueClass, String::class),
    ValueToStringConverter<Value> {

    /**
     * Called with an always non-null value to convert to a string.
     */
    override fun onConvert(from: Value): String = onToString(from)

    /**
     * Implemented by subclass to convert the given value to a string.
     */
    fun onToString(value: Value): String = value.toString()
}

package jonathanlocke.katalyst.convertase.conversion.converters.strings

import jonathanlocke.katalyst.convertase.conversion.converters.ConverterBase
import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import jonathanlocke.katalyst.cripsr.reflection.ValueClass.Companion.valueClass

/**
 * Base class for implementing converters from [Value] -> [String].
 *
 * **Extension Points**
 *
 * - [onToString] - Called with an always non-null value to convert it to a string
 *
 * **Inherited**
 *
 * - [nullValue] - The value to use for nullity if (a) nulls are not allowed, or (b) a conversion fails and the
 *                 error handler returns a null value instead of throwing an exception
 * - [nullAllowed] - True if null input values are allowed
 * - [problem] - Reports an error to the error handler
 *
 * @param Value The type to convert from
 * @see ConverterBase
 * @see ValueToStringConverter
 */
abstract class ValueToStringConverterBase<Value : Any>(valueClass: ValueClass<Value>) :
    ConverterBase<Value, String>(valueClass, valueClass(String::class)),
    ValueToStringConverter<Value> {

    /**
     * Called with an always non-null value to convert to a string.
     */
    final override fun onConvert(from: Value): String = onToString(from)

    /**
     * Implemented by subclass to convert the given value to a string.
     */
    fun onToString(value: Value): String = value.toString()
}

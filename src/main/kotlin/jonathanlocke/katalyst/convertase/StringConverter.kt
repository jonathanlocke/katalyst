package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.convertase.collections.ListConverter
import jonathanlocke.katalyst.convertase.collections.SetConverter

/**
 * A bidirectional converter between [String] values and values of the given type. The [Converter] interface
 * converts from [String] to type [T] and the method [unconvert] converts a [T] back to a [String].
 *
 * **Collections**
 *
 * - [toList]
 *
 * @param T The value to convert to
 * @see TwoWayConverterBase
 */
interface StringConverter<T : Any> : TwoWayConverter<String, T> {

    /**
     * Returns the given text, split on the given separator as a list of objects
     * @param text The text to convert
     * @param separator The separator
     * @return The list of objects
     */
    fun toList(text: String?, separator: String = ", "): List<T>? =
        ListConverter(this, separator).convert(text)

    /**
     * Returns the given text, split on the given separator as a list of objects
     * @param text The text to convert
     * @param separator The separator
     * @return The list of objects
     */
    fun toSet(text: String?, separator: String = ", "): Set<T>? =
        SetConverter(this, separator).convert(text)
}

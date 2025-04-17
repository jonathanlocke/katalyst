package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlerMixin

/**
 * A conversion supplies a "to" converter and a "from" converter
 *
 * - [converter] - The converter from [From] to [To]
 * - [reverseConverter] - The converter from [To] to [From]
 *
 * @param From The original type
 * @param To The desired type
 * @see Converter
 */
interface Conversion<From : Any, To : Any> : ErrorHandlerMixin<To> {

    /**
     * Returns the converter from the [From] type to the [To] type
     */
    fun converter(): Converter<From, To>

    /**
     * Returns the converter from the [To] type to the [From] type
     */
    fun reverseConverter(): Converter<To, From>
}

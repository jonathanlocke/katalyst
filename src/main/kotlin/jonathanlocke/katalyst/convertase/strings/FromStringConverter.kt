package jonathanlocke.katalyst.convertase.strings

import jonathanlocke.katalyst.convertase.Conversion
import jonathanlocke.katalyst.convertase.ConversionBase
import jonathanlocke.katalyst.convertase.Converter
import jonathanlocke.katalyst.convertase.strings.collections.ListConversion
import jonathanlocke.katalyst.convertase.strings.collections.SetConversion
import jonathanlocke.katalyst.convertase.strings.values.ToString
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import jonathanlocke.katalyst.nucleus.language.errors.strategies.Throw

/**
 * A bidirectional converter between [String] values and values of the given type. The [Converter] interface
 * converts from [String] to type [T] and the method [unconvert] converts a [T] back to a [String].
 *
 * **Collections**
 *
 * - [toList]
 *
 * @param T The value to convert to
 * @see ConversionBase
 */
interface FromStringConverter<T : Any> : Converter<String, T>, Conversion<String, T> {

    companion object {

        fun <T : Any> String.convert(
            converter: FromStringConverter<T>,
            errorHandler: ErrorHandler<T> = Throw()
        ): T? =
            converter.convert(this) ?: errorHandler.error("Cannot convert $this")

        fun <T : Any> String.convertToList(
            converter: FromStringConverter<T>,
            errorHandler: ErrorHandler<List<T>> = Throw(),
            parseSeparator: Regex = Regex(",\\s*"),
            joinSeparator: String = ", "
        ): List<T>? =
            ListConversion(converter, parseSeparator, joinSeparator)
                .to().errorHandler(errorHandler).convert(this)
    }

    override fun to(): Converter<String, T> = this

    override fun from(): Converter<T, String> = ToString()

    /**
     * Returns the given text, split on the given separator as a list of objects
     * @param text The text to convert
     * @param separator The separator
     * @return The list of objects
     */
    fun toList(text: String?, separator: String = ", "): List<T>? =
        ListConversion(this, separator).to().convert(text)

    /**
     * Returns the given text, split on the given separator as a list of objects
     * @param text The text to convert
     * @param separator The separator
     * @return The list of objects
     */
    fun toSet(text: String?, separator: String = ", "): Set<T>? =
        SetConversion(this, separator).to().convert(text)
}

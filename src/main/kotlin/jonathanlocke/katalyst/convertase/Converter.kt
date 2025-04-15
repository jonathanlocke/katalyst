package jonathanlocke.katalyst.convertase

import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandlingStrategy
import jonathanlocke.katalyst.nucleus.language.errors.ErrorReporter
import kotlin.reflect.KClass

/**
 * A converter converts from one type to another. Converters are [ErrorReporter]s, that report errors to an
 * interested party through an [ErrorHandlingStrategy].
 *
 * **Conversions**
 *
 * - [convert]
 * - [convertOrDefault]
 *
 * @param From Source type
 * @param To Destination type
 * @see ErrorReporter
 */
interface Converter<From : Any, To : Any> : ErrorReporter<From> {

    /**
     * Convert from type [From] to type [To].
     *
     * @param from The value to convert
     * @return The converted value
     */
    fun convert(from: From?): To?

    /**
     * Convert from type [From] to type [To], or return a default value if the conversion result is null.
     *
     * @param from The value to convert
     * @param defaultValue The default value to return if conversion results in null
     * @return The converted value or default value
     */
    fun convertOrDefault(from: From?, defaultValue: To): To = convert(from) ?: defaultValue

    /**
     * Returns the type that this converter converts from.
     */
    val fromType: KClass<From>

    /**
     * Returns the type that this converter converts to.
     */
    val toType: KClass<To>
}

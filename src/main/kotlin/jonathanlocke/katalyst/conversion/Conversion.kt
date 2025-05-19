package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.conversion.Conversion.Companion.conversion
import jonathanlocke.katalyst.conversion.Conversion.Companion.stringConversion
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.conversion.converters.ConverterLambda
import jonathanlocke.katalyst.conversion.converters.strings.StringConversion
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.status.StatusHandler

/**
 * A conversion supplies [From] -> [To] (forward) and [To] -> [From] (reverse) converters.
 *
 *  **Methods**
 *
 * - [register] - Registers this conversion with the [ConversionRegistry]
 * - [forwardConverter] - [From] -> [To] converter
 * - [reverseConverter] - [To] -> [From] converter]
 *
 *  **Companions**
 *
 * - [conversion] - Implements a [Conversion] using the given forward and reverse conversion lambdas
 * - [stringConversion] - Implements a [Conversion] from [String] -> Value and its reverse using the given lambdas
 *
 * @param From The source type
 * @param To The target type
 *
 * @see Converter
 * @see Conversion
 * @see ConversionRegistry
 */
interface Conversion<From : Any, To : Any> {

    val from: ValueType<From>
    val to: ValueType<To>

    /**
     * Registers this conversion with a [ConversionRegistry]
     */
    fun register(conversionRegistry: ConversionRegistry)

    /**
     * Returns the converter from the [From] type to the [To] type
     */
    fun forwardConverter(): Converter<From, To>

    /**
     * Returns the converter from the [To] type to the [From] type
     */
    fun reverseConverter(): Converter<To, From>

    companion object {

        /**
         * Implements a [Conversion] using the given forward and reverse conversion lambdas. This method
         * primarily provides syntactic sugar for creating a [Conversion] object.
         *
         * @param fromClass The class of the original type
         * @param toClass The class of the desired type
         * @param forwardConverterLambda Converter from [From] -> [To]
         * @param reverseConverterLambda Converter from [To] -> [From]
         * @return A [Conversion] that converts from [From] to [To]
         */
        @Suppress("UNCHECKED_CAST")
        fun <From : Any, To : Any> conversion(
            fromClass: ValueType<From>,
            toClass: ValueType<To>,
            forwardConverterLambda: (From?, StatusHandler) -> To?,
            reverseConverterLambda: (To?, StatusHandler) -> From?,
        ): Conversion<From, To> = object : ConversionBase<From, To>(fromClass, toClass) {

            override fun forwardConverter(): Converter<From, To> = object : Converter<From, To> {
                override val from = fromClass
                override val to = toClass
                override fun convert(from: From?, handler: StatusHandler): To? =
                    forwardConverterLambda(from, handler)
            }

            override fun reverseConverter(): Converter<To, From> = object : Converter<To, From> {
                override val from = toClass
                override val to = fromClass
                override fun convert(from: To?, handler: StatusHandler): From? =
                    reverseConverterLambda(from, handler)
            }
        }

        /**
         * Implements a bidirectional [String] <-> [Value] conversion using the given lambdas.
         *
         * @param type The class to convert to
         * @param valueToStringLambda Converter from [From] -> [String]
         * @param stringToValueLambda Converter from [String] -> [Value]
         * @return A bidirectional [String] <-> [Value] conversion
         */
        fun <Value : Any> stringConversion(
            type: ValueType<Value>,
            valueToStringLambda: ConverterLambda<String, Value>,
            stringToValueLambda: ConverterLambda<Value, String>,
        ): Conversion<String, Value> {
            return StringConversion(type, valueToStringLambda, stringToValueLambda)
        }
    }
}

package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.Conversion.Companion.conversion
import jonathanlocke.katalyst.convertase.conversion.Conversion.Companion.stringConversion
import jonathanlocke.katalyst.convertase.conversion.converters.Converter
import jonathanlocke.katalyst.convertase.conversion.converters.ConverterBase
import jonathanlocke.katalyst.cripsr.reflection.PropertyClass
import jonathanlocke.katalyst.cripsr.reflection.PropertyClass.Companion.valueClass
import jonathanlocke.katalyst.nucleus.problems.ProblemListener

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
 * @see jonathanlocke.katalyst.convertase.conversion.converters.Converter
 * @see Conversion
 * @see ConversionRegistry
 */
interface Conversion<From : Any, To : Any> {

    val from: PropertyClass<From>
    val to: PropertyClass<To>

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
            fromClass: PropertyClass<From>,
            toClass: PropertyClass<To>,
            forwardConverterLambda: (From?, ProblemListener) -> To?,
            reverseConverterLambda: (To?, ProblemListener) -> From?
        ): Conversion<From, To> = object : ConversionBase<From, To>(fromClass, toClass) {

            override fun forwardConverter(): Converter<From, To> = object : Converter<From, To> {
                override val from = fromClass
                override val to = toClass
                override fun convert(from: From?, listener: ProblemListener): To? =
                    forwardConverterLambda(from, listener)
            }

            override fun reverseConverter(): Converter<To, From> = object : Converter<To, From> {
                override val from = toClass
                override val to = fromClass
                override fun convert(from: To?, listener: ProblemListener): From? =
                    reverseConverterLambda(from, listener)
            }
        }

        /**
         * Implements a bidirectional [String] <-> [Value] conversion using the given lambdas.
         *
         * @param valueClass The value class (necessary due to type erasure)
         * @param valueToStringLambda Converter from [From] -> [String]
         * @param stringToValueLambda Converter from [String] -> [Value]
         * @return A bidirectional [String] <-> [Value] conversion
         */
        @Suppress("UNCHECKED_CAST")
        fun <Value : Any> stringConversion(
            valueClass: PropertyClass<Value>,
            valueToStringLambda: (Value?, ProblemListener) -> String? = { value, listener -> value.toString() },
            stringToValueLambda: (String, ProblemListener) -> Value?
        ): Conversion<String, Value> =
            object : ConversionBase<String, Value>(valueClass(String::class), valueClass) {

                override fun forwardConverter(): Converter<String, Value> = object : ConverterBase<String, Value>(
                    valueClass(String::class), valueClass
                ) {
                    override fun onConvert(from: String): Value? {
                        return stringToValueLambda(from, this)
                    }
                }

                override fun reverseConverter(): Converter<Value, String> = object : Converter<Value, String> {
                    override val from = valueClass
                    override val to = valueClass(String::class)
                    override fun convert(from: Value?, listener: ProblemListener): String? {
                        return valueToStringLambda(from, listener)
                    }
                }
            }
    }
}

package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.Conversion.Companion.conversion
import jonathanlocke.katalyst.nucleus.language.errors.ErrorHandler
import kotlin.reflect.KClass

/**
 * A conversion supplies a "to" converter and a "from" converter
 *
 * - [forwardConverter] - The converter from [From] to [To]
 * - [reverseConverter] - The converter from [To] to [From]
 * - [register] - Registers this conversion with the [ConversionRegistry]
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

    /**
     * Returns the converter from the [From] type to the [To] type
     */
    fun forwardConverter(): Converter<From, To>

    /**
     * Returns the converter from the [To] type to the [From] type
     */
    fun reverseConverter(): Converter<To, From>

    /**
     * Registers this conversion with the [ConversionRegistry]
     */
    fun register()

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
            fromClass: Class<From>,
            toClass: Class<To>,
            forwardConverterLambda: (From?, ErrorHandler<To?>) -> To?,
            reverseConverterLambda: (To?, ErrorHandler<From?>) -> From?
        ): Conversion<From, To> =
            object : ConversionBase<From, To>(fromClass.kotlin, toClass.kotlin) {

                override fun forwardConverter(): Converter<From, To> = object : Converter<From, To> {
                    override val fromClass = fromClass.kotlin
                    override val toClass = toClass.kotlin
                    override fun convert(from: From?, errorHandler: ErrorHandler<To?>): To? =
                        forwardConverterLambda(from, errorHandler)
                }

                override fun reverseConverter(): Converter<To, From> = object : Converter<To, From> {
                    override val fromClass = toClass.kotlin
                    override val toClass = fromClass.kotlin
                    override fun convert(from: To?, errorHandler: ErrorHandler<From?>): From? =
                        reverseConverterLambda(from, errorHandler)
                }
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
        valueClass: KClass<Value>,
        valueToStringLambda: (Value?, ErrorHandler<String?>) -> String? = { value, errorHandler -> value.toString() },
        stringToValueLambda: (String, ErrorHandler<Value?>) -> Value?
    ): Conversion<String, Value> =
        object : ConversionBase<String, Value>(String::class, valueClass) {

            override fun forwardConverter(): Converter<String, Value> = object : ConverterBase<String, Value>(
                String::class,
                valueClass
            ) {
                override fun onConvert(from: String): Value? {
                    return stringToValueLambda(from, this)
                }
            }

            override fun reverseConverter(): Converter<Value, String> = object : Converter<Value, String> {
                override val fromClass = valueClass
                override val toClass = String::class
                override fun convert(from: Value?, errorHandler: ErrorHandler<String?>): String? {
                    return valueToStringLambda(from, errorHandler)
                }
            }
        }
}

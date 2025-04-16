package jonathanlocke.katalyst.convertase

/**
 * Base class for implementing two-way converters.
 *
 * Adds to [ConverterBase] an implementation of [unconvert] that checks for null, catches
 * exceptions, and calls [onUnconvert].
 *
 * **Conversions**
 *
 * - [Converter.convert] - Called to convert *from* => *to*
 * - [unconvert] - Called to convert *to* => *from*
 *
 * **Implementing Converters**
 *
 * - [onConvert] - Called to convert *from* => *to*
 * - [onUnconvert] - Called to convert *to* => *from*
 *
 * **Missing Values**
 *
 * - [nullValue]
 *
 * @param From The type to convert from
 * @param To The type to convert to
 */
abstract class ConversionBase<From : Any, To : Any> : Conversion<From, To>

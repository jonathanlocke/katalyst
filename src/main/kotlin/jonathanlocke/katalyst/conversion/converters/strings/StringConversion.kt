package jonathanlocke.katalyst.conversion.converters.strings

import jonathanlocke.katalyst.conversion.ConversionBase
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.conversion.converters.ConverterBase
import jonathanlocke.katalyst.conversion.converters.ConverterLambda
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueTypeString


class StringConversion<Value : Any>(
    to: ValueType<Value>,
    private val stringToValueConverter: Converter<String, Value>,
    private val valueToStringConverter: Converter<Value, String>,
) : ConversionBase<String, Value>(valueTypeString, to) {

    constructor(
        to: ValueType<Value>,
        stringToValueLambda: ConverterLambda<String, Value>,
        valueToStringLambda: ConverterLambda<Value, String>,
    ) : this(
        to,
        object : ConverterBase<String, Value>(valueTypeString, to) {
            override fun onConvert(s: String): Value {
                return stringToValueLambda.convert(s, this)
            }
        },
        object : ConverterBase<Value, String>(to, valueTypeString) {
            override fun onConvert(value: Value): String {
                return valueToStringLambda.convert(value, this)
            }
        }
    )

    override fun forwardConverter(): Converter<String, Value> {
        return stringToValueConverter
    }

    override fun reverseConverter(): Converter<Value, String> {
        return valueToStringConverter
    }
}
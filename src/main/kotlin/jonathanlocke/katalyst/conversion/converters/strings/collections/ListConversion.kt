package jonathanlocke.katalyst.conversion.converters.strings.collections

import jonathanlocke.katalyst.conversion.Conversion
import jonathanlocke.katalyst.conversion.ConversionBase
import jonathanlocke.katalyst.conversion.converters.Converter
import jonathanlocke.katalyst.conversion.converters.ConverterBase
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.conversion.converters.strings.ValueToStringConverter
import jonathanlocke.katalyst.conversion.converters.strings.values.ValueToString
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ThrowOnError.Companion.throwOnError
import jonathanlocke.katalyst.reflection.ValueType
import jonathanlocke.katalyst.reflection.ValueType.Companion.valueType
import jonathanlocke.katalyst.text.parsing.Separator
import jonathanlocke.katalyst.text.parsing.Separator.Companion.commaSeparator

/**
 * A bidirectional [String] <-> List<[Value]> conversion.
 *
 * **Forward Conversion ([String] <-> List<[Value]>)**
 *
 *  - [stringToValueConverter] - The converter that converts [String] -> [Value]
 *  - [separator] - The separator to use when parsing text and joining value objects
 *  - [stringToValueProblemHandler] - The error to use problems with [String] -> [Value]
 *
 * **Reverse Conversion (List<[Value]> -> [String])**
 *
 *  - [valueToStringConverter] - The converter that converts [Value] -> [String]
 *  - [valueToStringProblemHandler] - The error to use problems with [Value] -> [String]**
 *  - [defaultToStringValue] - The value to use for null elements when converting to [String]
 *
 * @param Value The type of value to convert to and from
 * @property stringToValueConverter The converter that converts [String] -> [Value]
 * @property separator The separator to use when parsing text and joining value objects
 * @property stringToValueProblemHandler The error to use problems with [String] -> [Value]
 * @property valueToStringConverter The converter that converts [Value] -> [String]
 * @property valueToStringProblemHandler The error to use problems with [Value] -> [String]
 * @property defaultToStringValue The value to use for null elements when converting to [String]]
 *
 * @see Conversion
 * @see ConversionBase
 * @see StringToValueConverter
 * @see ValueToStringConverter
 * @see StringToValueConverter
 * @see Separator
 */
@Suppress("UNCHECKED_CAST")
class ListConversion<Value : Any>(

    // Value class
    val type: ValueType<Value>,

    // String -> Value conversion
    val stringToValueConverter: StringToValueConverter<Value>,
    val separator: Separator = commaSeparator,
    val stringToValueProblemHandler: ProblemHandler = throwOnError,

    // Value -> String conversion
    val valueToStringConverter: ValueToStringConverter<Value> = ValueToString(type) as ValueToStringConverter<Value>,
    val valueToStringProblemHandler: ProblemHandler = throwOnError,
    val defaultToStringValue: String = "?"

) : ConversionBase<String, List<Value>>(
    valueType(String::class),
    valueType(List::class) as ValueType<List<Value>>
) {

    @Suppress("UNCHECKED_CAST")
    override fun forwardConverter(): StringToValueConverter<List<Value>> = stringToValueConverter(
        valueType(List::class) as ValueType<List<Value>>
    ) { text, problemHandler ->
        separator.split(text).map { member ->
            stringToValueConverter.convert(member, stringToValueProblemHandler)
                ?: stringToValueProblemHandler.error("Failed to convert element: $member")
        } as List<Value>?
    }

    override fun reverseConverter(): Converter<List<Value>, String> = object :
        ConverterBase<List<Value>, String>(List::class as ValueType<List<Value>>, valueType(String::class)) {
        override fun onConvert(from: List<Value>): String = separator.join(from.map {
            valueToStringConverter.convert(it, valueToStringProblemHandler) ?: defaultToStringValue
        }.toList())
    }
}

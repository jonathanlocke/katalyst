package jonathanlocke.katalyst.convertase.conversion.converters.strings.collections

import jonathanlocke.katalyst.convertase.conversion.Conversion
import jonathanlocke.katalyst.convertase.conversion.ConversionBase
import jonathanlocke.katalyst.convertase.conversion.converters.Converter
import jonathanlocke.katalyst.convertase.conversion.converters.ConverterBase
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.ValueToStringConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.values.ValueToString
import jonathanlocke.katalyst.cripsr.reflection.ValueClass
import jonathanlocke.katalyst.cripsr.reflection.ValueClass.Companion.valueClass
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw

/**
 * A bidirectional [String] <-> List<[Value]> conversion.
 *
 * **Forward Conversion ([String] <-> List<[Value]>)**
 *
 *  - [stringToValueConverter] - The converter that converts [String] -> [Value]
 *  - [separator] - The separator to use when parsing text and joining value objects
 *  - [stringToValueReporter] - The error to use problems with [String] -> [Value]
 *
 * **Reverse Conversion (List<[Value]> -> [String])**
 *
 *  - [valueToStringConverter] - The converter that converts [Value] -> [String]
 *  - [valueToStringReporter] - The error to use problems with [Value] -> [String]**
 *  - [defaultToStringValue] - The value to use for null elements when converting to [String]
 *
 * @param Value The type of value to convert to and from
 * @property stringToValueConverter The converter that converts [String] -> [Value]
 * @property separator The separator to use when parsing text and joining value objects
 * @property stringToValueReporter The error to use problems with [String] -> [Value]
 * @property valueToStringConverter The converter that converts [Value] -> [String]
 * @property valueToStringReporter The error to use problems with [Value] -> [String]
 * @property defaultToStringValue The value to use for null elements when converting to [String]]
 *
 * @see Conversion
 * @see StringToValueConverter
 * @see ValueToStringConverter
 * @see StringToValueConverter
 * @see Separator
 */
@Suppress("UNCHECKED_CAST")
class ListConversion<Value : Any>(

    // Value class
    val valueClass: ValueClass<Value>,

    // String -> Value conversion
    val stringToValueConverter: StringToValueConverter<Value>,
    val separator: Separator = Separator(),
    val stringToValueReporter: ProblemListener = Throw(),

    // Value -> String conversion
    val valueToStringConverter: ValueToStringConverter<Value> = ValueToString(valueClass) as ValueToStringConverter<Value>,
    val valueToStringReporter: ProblemListener = Throw(),
    val defaultToStringValue: String = "?"

) : ConversionBase<String, List<Value>>(valueClass(String::class), List::class as ValueClass<List<Value>>) {

    @Suppress("UNCHECKED_CAST")
    override fun forwardConverter(): StringToValueConverter<List<Value>> = stringToValueConverter(
        List::class as ValueClass<List<Value>>
    ) { text, listener ->
        separator.split(text).map { member ->
            stringToValueConverter.convert(member, stringToValueReporter)
                ?: stringToValueReporter.error("Failed to convert element: $member")
        } as List<Value>?
    }

    override fun reverseConverter(): Converter<List<Value>, String> = object : ConverterBase<List<Value>, String>
        (List::class as ValueClass<List<Value>>, valueClass(String::class)) {
        override fun onConvert(from: List<Value>): String = separator.join(from.map {
            valueToStringConverter.convert(it, valueToStringReporter) ?: defaultToStringValue
        }.toList())
    }
}

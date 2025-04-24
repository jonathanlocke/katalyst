package jonathanlocke.katalyst.convertase.conversion.converters.strings.collections

import jonathanlocke.katalyst.convertase.conversion.Conversion
import jonathanlocke.katalyst.convertase.conversion.ConversionBase
import jonathanlocke.katalyst.convertase.conversion.converters.Converter
import jonathanlocke.katalyst.convertase.conversion.converters.ConverterBase
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.ValueToStringConverter
import jonathanlocke.katalyst.convertase.conversion.converters.strings.values.ValueToString
import jonathanlocke.katalyst.cripsr.reflection.PropertyClass
import jonathanlocke.katalyst.cripsr.reflection.PropertyClass.Companion.valueClass
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
 *  - [stringToValueProblemListener] - The error to use problems with [String] -> [Value]
 *
 * **Reverse Conversion (List<[Value]> -> [String])**
 *
 *  - [valueToStringConverter] - The converter that converts [Value] -> [String]
 *  - [valueToStringProblemListener] - The error to use problems with [Value] -> [String]**
 *  - [defaultToStringValue] - The value to use for null elements when converting to [String]
 *
 * @param Value The type of value to convert to and from
 * @property stringToValueConverter The converter that converts [String] -> [Value]
 * @property separator The separator to use when parsing text and joining value objects
 * @property stringToValueProblemListener The error to use problems with [String] -> [Value]
 * @property valueToStringConverter The converter that converts [Value] -> [String]
 * @property valueToStringProblemListener The error to use problems with [Value] -> [String]
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
    val valueClass: PropertyClass<Value>,

    // String -> Value conversion
    val stringToValueConverter: StringToValueConverter<Value>,
    val separator: Separator = Separator(),
    val stringToValueProblemListener: ProblemListener = Throw(),

    // Value -> String conversion
    val valueToStringConverter: ValueToStringConverter<Value> = ValueToString(valueClass) as ValueToStringConverter<Value>,
    val valueToStringProblemListener: ProblemListener = Throw(),
    val defaultToStringValue: String = "?"

) : ConversionBase<String, List<Value>>(valueClass(String::class), List::class as PropertyClass<List<Value>>) {

    @Suppress("UNCHECKED_CAST")
    override fun forwardConverter(): StringToValueConverter<List<Value>> = stringToValueConverter(
        List::class as PropertyClass<List<Value>>
    ) { text, listener ->
        separator.split(text).map { member ->
            stringToValueConverter.convert(member, stringToValueProblemListener)
                ?: stringToValueProblemListener.error("Failed to convert element: $member")
        } as List<Value>?
    }

    override fun reverseConverter(): Converter<List<Value>, String> = object : ConverterBase<List<Value>, String>
        (List::class as PropertyClass<List<Value>>, valueClass(String::class)) {
        override fun onConvert(from: List<Value>): String = separator.join(from.map {
            valueToStringConverter.convert(it, valueToStringProblemListener) ?: defaultToStringValue
        }.toList())
    }
}

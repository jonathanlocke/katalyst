package jonathanlocke.katalyst.convertase.conversion.strings.collections

import jonathanlocke.katalyst.convertase.conversion.Conversion
import jonathanlocke.katalyst.convertase.conversion.ConversionBase
import jonathanlocke.katalyst.convertase.conversion.Converter
import jonathanlocke.katalyst.convertase.conversion.ConverterBase
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.strings.StringToValueConverter.Companion.stringToValueConverter
import jonathanlocke.katalyst.convertase.conversion.strings.ValueToStringConverter
import jonathanlocke.katalyst.convertase.conversion.strings.values.ValueToString
import jonathanlocke.katalyst.nucleus.language.problems.ProblemReporter
import jonathanlocke.katalyst.nucleus.language.problems.reporters.Throw
import jonathanlocke.katalyst.nucleus.language.strings.parsing.Separator
import kotlin.reflect.KClass

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
    val valueClass: KClass<Value>,

    // String -> Value conversion
    val stringToValueConverter: StringToValueConverter<Value>,
    val separator: Separator = Separator(),
    val stringToValueReporter: ProblemReporter<Value> = Throw(),

    // Value -> String conversion
    val valueToStringConverter: ValueToStringConverter<Value> = ValueToString(valueClass) as ValueToStringConverter<Value>,
    val valueToStringReporter: ProblemReporter<String> = Throw(),
    val defaultToStringValue: String = "?"

) : ConversionBase<String, List<Value>>(String::class, List::class as KClass<List<Value>>) {

    @Suppress("UNCHECKED_CAST")
    override fun forwardConverter(): StringToValueConverter<List<Value>> = stringToValueConverter(
        List::class as KClass<List<Value>>
    ) { text, reporter ->
        separator.split(text).map { member ->
            stringToValueConverter.convert(member, stringToValueReporter)
                ?: stringToValueReporter.error("Failed to convert element: $member")
        } as List<Value>?
    }

    override fun reverseConverter(): Converter<List<Value>, String> = object : ConverterBase<List<Value>, String>
        (List::class as KClass<List<Value>>, String::class) {
        override fun onConvert(from: List<Value>): String = separator.join(from.map {
            valueToStringConverter.convert(it, valueToStringReporter) ?: defaultToStringValue
        }.toList())
    }
}

package jonathanlocke.katalyst.conversion

import jonathanlocke.katalyst.conversion.converters.strings.values.StringToNumber

class DefaultConversionRegistry : ConversionRegistry() {

    init {
        registerAllConversions(StringToNumber)
    }
}

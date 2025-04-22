package jonathanlocke.katalyst.convertase.conversion

import jonathanlocke.katalyst.convertase.conversion.converters.strings.values.StringToNumber

class DefaultConversionRegistry : ConversionRegistry() {

    init {
        StringToNumber.registerConversions(this)
        println(this)
    }
}
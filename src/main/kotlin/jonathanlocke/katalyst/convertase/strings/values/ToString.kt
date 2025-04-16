package jonathanlocke.katalyst.convertase.strings.values

import jonathanlocke.katalyst.convertase.ConverterBase

class ToString<From : Any> : ConverterBase<From, String>() {

    override fun onConvert(from: From): String = from.toString()
}

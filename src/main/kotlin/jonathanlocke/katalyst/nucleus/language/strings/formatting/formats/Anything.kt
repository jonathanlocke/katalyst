package jonathanlocke.katalyst.nucleus.language.strings.formatting.formats

import jonathanlocke.katalyst.nucleus.language.strings.formatting.StringFormatter

class Anything {

    companion object {

        fun <T> convertToString() = StringFormatter<T> { it.toString() }
    }
}

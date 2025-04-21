package jonathanlocke.katalyst.nucleus.language.primitives

import jonathanlocke.katalyst.nucleus.language.functional.Action
import java.util.function.Consumer

class Ints {

    fun Int.loop(code: Action) = (0 until this).forEach { code.execute() }
    fun Int.loop(code: Consumer<Int>) = (0 until this).forEach { code.accept(it) }
}

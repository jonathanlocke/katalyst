package jonathanlocke.katalyst.nucleus.language.primitives

import java.util.function.Consumer

class Ints {

    fun Int.loop(code: Runnable) = (0 until this).forEach { code.run() }
    fun Int.loop(code: Consumer<Int>) = (0 until this).forEach { code.accept(it) }
}

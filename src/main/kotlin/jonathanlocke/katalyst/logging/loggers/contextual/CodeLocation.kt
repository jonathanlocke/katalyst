package jonathanlocke.katalyst.logging.loggers.contextual

import jonathanlocke.katalyst.logging.Logger
import jonathanlocke.katalyst.text.formatting.Formattable
import java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE
import java.lang.StackWalker.StackFrame
import java.util.function.Predicate

class CodeLocation(val type: Class<*>, val methodName: String, val lineNumber: Int) : Formattable<CodeLocation> {

    companion object {

        private val stackWalker: StackWalker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE)

        val defaultFilter = Predicate<StackFrame> { it.className.startsWith(Logger::class.java.packageName) }

        fun codeContext(): CodeLocation = codeContext(defaultFilter)

        fun codeContext(filter: Predicate<StackFrame>): CodeLocation {
            val caller = stackWalker.walk { stackFrames ->
                stackFrames.skip(1).filter(filter).findFirst().orElse(null)
            }
            return CodeLocation(caller.declaringClass, caller.methodName, caller.lineNumber)
        }
    }
}

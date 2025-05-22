package jonathanlocke.katalyst.logging.loggers.contextual

import jonathanlocke.katalyst.text.formatting.Formattable
import java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE
import java.lang.StackWalker.StackFrame
import java.util.function.Predicate

class CodeContext(val type: Class<*>, val methodName: String, val lineNumber: Int) : Formattable<CodeContext> {

    override fun toString(): String = "${type.simpleName}.$methodName:$lineNumber"

    companion object {

        private val stackWalker: StackWalker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE)

        var includePattern = listOf<String>()
        var excludePackages = listOf(
            "org.junit",
            "org.gradle",
            "worker.org.gradle",
            "org.testng",
            "sun.reflect",
            "kotlin",
            "kotlinx",
            "org.jetbrains",
            "org.apache.maven",
            "org.codehaus.plexus",
            "jdk",
            "java",
            "jonathanlocke.katalyst.logging",
            "jonathanlocke.katalyst.mixins",
            "jonathanlocke.katalyst.status"
        )

        val defaultFilter = Predicate<StackFrame> { frame ->
            val packageName = frame.declaringClass.packageName
            (includePattern.isEmpty() || includePattern.any { packageName.startsWith(it) }) &&
                    excludePackages.none { packageName.startsWith(it) }
        }

        fun codeContext(filter: Predicate<StackFrame> = defaultFilter): CodeContext {
            val caller = stackWalker.walk { stackFrames ->
                stackFrames.skip(1).filter(filter).findFirst().orElse(null)
            }
            return if (caller == null) {
                CodeContext(CodeContext::class.java, "unknown", -1)
            } else {
                CodeContext(caller.declaringClass, caller.methodName, caller.lineNumber)
            }
        }
    }
}

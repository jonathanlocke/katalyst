package jonathanlocke.katalyst.nucleus.progress

import jonathanlocke.katalyst.nucleus.values.percent.Percent

fun interface ProgressListener {
    fun at(at: Percent)
}

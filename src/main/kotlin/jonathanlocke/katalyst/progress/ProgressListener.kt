package jonathanlocke.katalyst.progress

import jonathanlocke.katalyst.data.values.numeric.percent.Percent

fun interface ProgressListener {
    fun at(at: Percent)
}

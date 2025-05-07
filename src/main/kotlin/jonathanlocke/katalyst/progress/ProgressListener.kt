package jonathanlocke.katalyst.progress

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.percent.Percent

interface ProgressListener {
    fun progress(at: Percent)
    fun progress(at: Count)
}

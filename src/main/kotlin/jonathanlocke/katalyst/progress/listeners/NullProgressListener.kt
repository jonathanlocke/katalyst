package jonathanlocke.katalyst.progress.listeners

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.progress.ProgressListener

class NullProgressListener : ProgressListener {
    override fun progress(at: Percent) {
    }

    override fun progress(at: Count) {
    }
}
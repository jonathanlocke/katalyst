package jonathanlocke.katalyst.progress

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.percent

/**
 * Reports the progress of an operation
 */
class ProgressReporter(val progressListener: ProgressListener, val steps: Count = count(100)) {

    var at = count(0)
    fun percentComplete() = percent(at * 100.0 / steps)

    fun next() {
        progressListener.at(percentComplete())
        at++
    }

    fun next(steps: Count) = steps.loop { next() }
}

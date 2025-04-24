package jonathanlocke.katalyst.progress

import jonathanlocke.katalyst.data.values.count.Count
import jonathanlocke.katalyst.data.values.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.percent.Percent.Companion.percent

/**
 * Reports the progress of an operation
 */
class ProgressReporter(val listener: ProgressListener, val steps: Count = count(100)) {

    var at = count(0)
    fun percentComplete() = percent(100.0 * at.asDouble() / steps.asDouble())

    fun next() {
        listener.at(percentComplete())
        at = at + 1
    }

    fun next(steps: Count) = steps.loop { next() }
}

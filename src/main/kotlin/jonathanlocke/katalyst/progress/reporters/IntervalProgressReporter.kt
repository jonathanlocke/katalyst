package jonathanlocke.katalyst.progress.reporters

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.percent
import jonathanlocke.katalyst.progress.ProgressListener
import jonathanlocke.katalyst.progress.ProgressReporter

/**
 * Reports the progress of an operation
 */
open class IntervalProgressReporter(
    val progressListener: ProgressListener,
    val operationSteps: Count? = null,
    val reportEvery: Count = count(10_000),
    val reportEveryPercent: Percent = percent(10),
) : ProgressReporter {
    var step = 0

    private var lastPercentReported: Percent? = null

    override fun next() {

        // If we should call back,
        if (step % reportEvery.asInt() == 0) {

            // then call the listener with the step number.
            progressListener.progress(count(step))
        }

        // If we should call back with a percent completed,
        if (operationSteps != null) {

            // compute the percent complete,
            val percentComplete = percent(step * 100.0 / operationSteps.asInt())

            // and if we haven't already reported this percent complete and its due to be reported,
            if (percentComplete != lastPercentReported &&
                percentComplete.percent % reportEveryPercent.percent == 0.0
            ) {
                progressListener.progress(percentComplete)
                lastPercentReported = percentComplete
            }
        }

        step++
    }

    override fun next(steps: Count) = steps.loop { next() }
}

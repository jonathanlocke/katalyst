package jonathanlocke.katalyst.progress

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.count
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.data.values.numeric.percent.Percent.Companion.percent
import jonathanlocke.katalyst.progress.listeners.ProblemProgressListener
import jonathanlocke.katalyst.progress.reporters.IntervalProgressReporter
import jonathanlocke.katalyst.progress.reporters.NullProgressReporter
import jonathanlocke.katalyst.status.StatusHandler

/**
 * Reports the progress of an operation
 */
interface ProgressReporter {

    companion object {

        val nullProgressReporter = NullProgressReporter()

        fun progressReporter(
            statusHandler: StatusHandler,
            operationSteps: Count,
            reportEvery: Count = count(10_000),
            reportEveryPercent: Percent = percent(10),
        ) = IntervalProgressReporter(ProblemProgressListener(statusHandler), operationSteps, reportEvery)
    }

    fun next()
    fun next(steps: Count) = steps.loop { next() }
}

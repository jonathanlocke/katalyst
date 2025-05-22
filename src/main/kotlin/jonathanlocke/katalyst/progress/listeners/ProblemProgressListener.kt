package jonathanlocke.katalyst.progress.listeners

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.ThousandsSeparatedFormatter
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.progress.ProgressListener
import jonathanlocke.katalyst.status.StatusHandler

class ProblemProgressListener(private val statusHandler: StatusHandler) : ProgressListener {

    override fun progress(at: Percent) {
        statusHandler.info("${at}%")
    }

    override fun progress(at: Count) {
        statusHandler.info(at.format(ThousandsSeparatedFormatter))
    }
}

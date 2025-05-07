package jonathanlocke.katalyst.progress.listeners

import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.data.values.numeric.count.Count.Companion.ThousandsSeparatedFormatter
import jonathanlocke.katalyst.data.values.numeric.percent.Percent
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.progress.ProgressListener

class ProblemProgressListener(private val problemHandler: ProblemHandler) : ProgressListener {

    override fun progress(at: Percent) {
        problemHandler.info("${at}%")
    }

    override fun progress(at: Count) {
        problemHandler.info(at.format(ThousandsSeparatedFormatter))
    }
}
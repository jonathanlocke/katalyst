package jonathanlocke.katalyst.progress.reporters

import jonathanlocke.katalyst.progress.ProgressReporter

class NullProgressReporter : ProgressReporter {
    override fun next() {
    }
}
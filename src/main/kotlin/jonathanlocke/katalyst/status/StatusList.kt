package jonathanlocke.katalyst.status

import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.globalMaximumSize
import jonathanlocke.katalyst.data.structures.SafeDataStructure.Companion.safeList
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.status.Status.Effect.CONTINUE
import jonathanlocke.katalyst.status.Status.Effect.STOP
import jonathanlocke.katalyst.status.StatusFormatters.Companion.statusListDetailsFormatter
import jonathanlocke.katalyst.text.formatting.Formattable

class NullStatusList : StatusList() {

    override val size = 0
    override fun add(index: Int, element: Status) {}
}

/**
 * A [MutableList] of [Status]s.
 *
 * @see Status
 */
open class StatusList(
    val maximumProblems: Count = globalMaximumSize,
) : AbstractMutableList<Status>(), StatusHandler, Formattable<StatusList> {

    private val statuses: MutableList<Status> by lazy { safeList("statuses", maximumSize = maximumProblems) }

    override fun statuses() = this
    override fun handlers(): MutableList<StatusHandler> = throw unimplemented()
    override fun handle(status: Status) {
        statuses.add(status)
    }

    fun count() = statuses.size
    fun errors() = statuses.filter { it.effect == STOP }.size
    fun warnings() = statuses.filter { it.effect == CONTINUE }.size
    fun isValid() = errors() == 0
    fun isInvalid() = !isValid()

    override fun toString(): String = statusListDetailsFormatter.format(this)

    override fun set(index: Int, element: Status): Status = statuses.set(index, element)
    override fun removeAt(index: Int): Status = statuses.removeAt(index)
    override fun add(index: Int, element: Status) = statuses.add(index, element)
    override val size: Int get() = statuses.size
    override fun get(index: Int): Status = statuses.get(index)
}

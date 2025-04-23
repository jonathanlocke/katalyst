package jonathanlocke.katalyst.nucleus.language.collections.lists

import jonathanlocke.katalyst.nucleus.language.collections.SafeDataStructure
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.values.count.Count
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.toCount

/**
 * A [MutableList] that is safe to use.
 *
 * @param metadata Metadata about the collection
 * @param problemListener The problem listener to use
 * @param list The underlying list
 *
 * @see SafetyMetadata
 * @see ProblemListener
 * @see SafeDataStructure
 */
class SafeMutableList<Element : Any>(
    override val metadata: SafetyMetadata,
    override val problemListener: ProblemListener = Throw(),
    private val list: MutableList<Element>
) : SafeDataStructure(metadata, problemListener), MutableList<Element> by list {

    override fun size(): Count = list.size.toCount()

    fun toImmutableList(): List<Element> = list.toList()

    override fun add(element: Element): Boolean {
        ensureSafeToAdd(1)
        return list.add(element)
    }

    override fun addAll(elements: Collection<Element>): Boolean {
        ensureSafeToAdd(elements.size)
        return list.addAll(elements)
    }

    override fun addFirst(e: Element) {
        ensureSafeToAdd(1)
        return list.addFirst(e)
    }

    override fun addLast(e: Element) {
        ensureSafeToAdd(1)
        return list.addLast(e)
    }

    override fun add(index: Int, element: Element) {
        ensureSafeToAdd(1)
        return list.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<Element>): Boolean {
        ensureSafeToAdd(elements.size)
        return list.addAll(index, elements)
    }
}
package jonathanlocke.katalyst.data.structures.lists

import jonathanlocke.katalyst.data.structures.SafeDataStructure
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.status.StatusHandler

/**
 * A [MutableList] that is safe to use.
 *
 * @param metadata Metadata about the collection
 * @param statusHandler The status handler to use
 * @param list The underlying list
 *
 * @see SafetyMetadata
 * @see StatusHandler
 * @see SafeDataStructure
 */
class SafeList<Element : Any> internal constructor(
    override val statusHandler: StatusHandler,
    override val metadata: SafetyMetadata,
    private val newUnsafeList: (Count) -> MutableList<Element>,
) : SafeDataStructure(statusHandler, metadata), MutableList<Element> {

    private val list = newUnsafeList(metadata.initialSize)

    fun copy(): SafeList<Element> {
        val copy = SafeList(statusHandler, metadata, newUnsafeList)
        copy.addAll(this as MutableCollection<out Element>)
        return copy
    }

    fun toImmutableList(): List<Element> = list.toList()

    fun with(element: Element): SafeList<Element> {
        val copy: SafeList<Element> = copy()
        copy.add(element)
        return copy
    }

    fun with(elements: Iterable<Element>): SafeList<Element> {
        val copy: SafeList<Element> = copy()
        copy.addAll(elements)
        return copy
    }

    override fun add(element: Element): Boolean {
        ensureSafeToAdd(1)
        return list.add(element)
    }

    override fun addAll(elements: Collection<Element>): Boolean {
        ensureSafeToAdd(elements.size)
        return list.addAll(elements)
    }

    fun addAll(elements: Iterable<Element>): Boolean {
        elements.forEach { add(it) }
        return true
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

    override val size: Int get() = list.size
    override fun clear() = list.clear()
    override fun contains(element: Element): Boolean = list.contains(element)
    override fun containsAll(elements: Collection<Element>): Boolean = list.containsAll(elements)
    override fun get(index: Int): Element = list[index]
    override fun indexOf(element: Element): Int = list.indexOf(element)
    override fun isEmpty(): Boolean = list.isEmpty()
    override fun iterator(): MutableIterator<Element> = list.iterator()
    override fun lastIndexOf(element: Element): Int = list.lastIndexOf(element)
    override fun listIterator(): MutableListIterator<Element> = list.listIterator()
    override fun listIterator(index: Int): MutableListIterator<Element> = list.listIterator(index)
    override fun remove(element: Element): Boolean = list.remove(element)
    override fun removeAll(elements: Collection<Element>): Boolean = list.removeAll(elements)
    override fun removeAt(index: Int): Element = list.removeAt(index)
    override fun retainAll(elements: Collection<Element>): Boolean = list.retainAll(elements)
    override fun set(index: Int, element: Element): Element = list.set(index, element)
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Element> = list.subList(fromIndex, toIndex)
}

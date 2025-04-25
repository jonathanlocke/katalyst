package jonathanlocke.katalyst.data.structures.sets

import jonathanlocke.katalyst.data.structures.SafeDataStructure
import jonathanlocke.katalyst.problems.ProblemListener
import jonathanlocke.katalyst.problems.listeners.ThrowOnError.Companion.throwOnError

/**
 * A [MutableSet] that is safe to use.
 *
 * @param metadata Metadata about the collection
 * @param problemListener The problem listener to use
 * @param set The underlying set
 *
 * @see SafetyMetadata
 * @see ProblemListener
 */
class SafeSet<Member : Any> internal constructor(
    override val metadata: SafetyMetadata,
    override val problemListener: ProblemListener = throwOnError,
    private val set: MutableSet<Member>
) : SafeDataStructure(metadata, problemListener), MutableSet<Member> {

    fun toImmutableSet(): Set<Member> = set.toSet()

    override fun add(element: Member): Boolean {
        ensureSafeToAdd(1)
        return set.add(element)
    }

    override fun addAll(elements: Collection<Member>): Boolean {
        ensureSafeToAdd(elements.size)
        return set.addAll(elements)
    }

    override val size: Int get() = set.size
    override fun clear() = set.clear()
    override fun isEmpty(): Boolean = set.isEmpty()
    override fun contains(element: Member): Boolean = set.contains(element)
    override fun containsAll(elements: Collection<Member>): Boolean = set.containsAll(elements)
    override fun iterator(): MutableIterator<Member> = set.iterator()
    override fun retainAll(elements: Collection<Member>): Boolean = set.retainAll(elements)
    override fun removeAll(elements: Collection<Member>): Boolean = set.removeAll(elements)
    override fun remove(element: Member): Boolean = set.remove(element)
}
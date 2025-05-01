package jonathanlocke.katalyst.data.structures.sets

import jonathanlocke.katalyst.data.structures.SafeDataStructure
import jonathanlocke.katalyst.problems.ProblemHandler
import jonathanlocke.katalyst.problems.handlers.ThrowOnError.Companion.throwOnError

/**
 * A [MutableSet] that is safe to use.
 *
 * @param metadata Metadata about the collection
 * @param problemHandler The problem handler to use
 * @param set The underlying set
 *
 * @see SafetyMetadata
 * @see ProblemHandler
 */
class SafeSet<Member : Any> internal constructor(
    override val metadata: SafetyMetadata,
    override val problemHandler: ProblemHandler = throwOnError,
    private val set: MutableSet<Member>
) : SafeDataStructure(metadata, problemHandler), MutableSet<Member> {

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
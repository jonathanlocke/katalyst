package jonathanlocke.katalyst.nucleus.language.collections.lists

import jonathanlocke.katalyst.nucleus.language.collections.SafeDataStructure
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.toCount

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
class SafeMutableSet<Member : Any>(
    override val metadata: SafetyMetadata,
    override val problemListener: ProblemListener = Throw(),
    private val set: MutableSet<Member>
) : SafeDataStructure(metadata, problemListener), MutableSet<Member> by set {

    override fun size() = set.size.toCount()

    fun toImmutableSet(): Set<Member> = set.toSet()

    override fun add(element: Member): Boolean {
        ensureSafeToAdd(1)
        return set.add(element)
    }

    override fun addAll(elements: Collection<Member>): Boolean {
        ensureSafeToAdd(elements.size)
        return set.addAll(elements)
    }
}
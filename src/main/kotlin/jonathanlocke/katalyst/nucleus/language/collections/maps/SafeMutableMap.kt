package jonathanlocke.katalyst.nucleus.language.collections.maps

import jonathanlocke.katalyst.nucleus.language.collections.SafeDataStructure
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.values.count.Count.Companion.toCount

/**
 * A [MutableSet] that is safe to use.
 *
 * @param metadata Metadata about the collection
 * @param problemListener The problem listener to use
 * @param map The underlying set
 *
 * @see SafetyMetadata
 * @see ProblemListener
 */
class SafeMutableMap<Key : Any, Value>(
    override val metadata: SafetyMetadata,
    override val problemListener: ProblemListener = Throw(),
    private val map: MutableMap<Key, Value> = HashMap(metadata.estimatedSize.asInt())
) : SafeDataStructure(metadata, problemListener), MutableMap<Key, Value> by map {

    override fun size() = map.size.toCount()

    fun toImmutableMap(): Map<Key, Value> = map.toMap()
    
    override fun put(key: Key, value: Value): Value? {
        ensureSafeToAdd(1)
        return map.put(key, value)
    }

    override fun putIfAbsent(key: Key, value: Value): Value? {
        ensureSafeToAdd(1)
        return map.putIfAbsent(key, value)
    }

    override fun putAll(from: Map<out Key, Value>) {
        ensureSafeToAdd(from.size)
        return map.putAll(from)
    }
}
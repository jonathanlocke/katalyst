package jonathanlocke.katalyst.nucleus.language.collections.maps

import jonathanlocke.katalyst.nucleus.language.collections.SafeDataStructure
import jonathanlocke.katalyst.nucleus.language.collections.lists.SafeMutableList
import jonathanlocke.katalyst.nucleus.problems.ProblemListener
import jonathanlocke.katalyst.nucleus.problems.listeners.Throw
import jonathanlocke.katalyst.nucleus.values.count.Count
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
class SafeMutableMultiMap<Key : Any, Value : Any>(
    override val metadata: SafetyMetadata,
    override val problemListener: ProblemListener = Throw(),
    private val newUnsafeMutableList: (Count) -> MutableList<Value> = { size -> ArrayList(size.asInt()) },
    internal val map: SafeMutableMap<Key, SafeMutableList<Value>> = SafeMutableMap(metadata, problemListener)
) : SafeDataStructure(metadata, problemListener) {

    var totalSize = 0

    override fun size() = totalSize.toCount()

    fun put(key: Key, value: Value) {
        ensureSafeToAdd(1)
        map.getOrPut(key) {
            SafeMutableList(metadata, problemListener, newUnsafeMutableList.invoke(metadata.estimatedSize))
        }.add(value)
        totalSize += 1
    }

    fun putAll(key: Key, values: Collection<Value>) {
        ensureSafeToAdd(values.size)
        map.getOrPut(key) {
            SafeMutableList(metadata, problemListener, newUnsafeMutableList.invoke(metadata.estimatedSize))
        }.addAll(values)
        totalSize += values.size
    }

    fun putAll(multiMap: SafeMutableMultiMap<Key, Value>) = {
        ensureSafeToAdd(multiMap.totalSize)
        multiMap.map.forEach { (key, values) -> putAll(key, values) }
        totalSize += multiMap.totalSize
    }

    operator fun get(key: Key): List<Value>? {
        return map[key]
    }

    fun remove(key: Key, value: Value): Boolean {
        val values = map[key] ?: return false
        val removed = values.remove(value)
        totalSize--
        if (values.isEmpty()) {
            map.remove(key)
        }
        return removed
    }

    fun removeAll(key: Key): List<Value>? {
        totalSize -= map[key]?.size ?: 0
        return map.remove(key)
    }
}
package jonathanlocke.katalyst.data.structures.maps

import jonathanlocke.katalyst.data.structures.SafeDataStructure
import jonathanlocke.katalyst.data.structures.lists.SafeList
import jonathanlocke.katalyst.status.StatusHandler
import jonathanlocke.katalyst.status.StatusHandlers.Companion.throwOnError

/**
 * A [MutableSet] that is safe to use.
 *
 * @param metadata Metadata about the collection
 * @param statusHandler The status handler to use
 * @param map The underlying set
 *
 * @see SafetyMetadata
 * @see StatusHandler
 */
class SafeMultiMap<Key : Any, Value : Any> internal constructor(
    override val metadata: SafetyMetadata,
    override val statusHandler: StatusHandler = throwOnError,
    private val newSafeList: () -> SafeList<Value>,
    internal val map: SafeMap<Key, SafeList<Value>> = SafeMap(metadata, statusHandler),
) : SafeDataStructure(metadata, statusHandler) {

    var totalSize = 0

    override val size = totalSize

    fun entries(): List<Pair<Key, List<Value>>> = map.map { it.key to it.value.toList() }

    fun containsKey(key: Key): Boolean = map.containsKey(key)

    fun containsEntry(key: Key, value: Value): Boolean = map[key]?.contains(value) ?: false

    fun put(key: Key, value: Value) {
        ensureSafeToAdd(1)
        map.getOrPut(key) {
            newSafeList.invoke()
        }.add(value)
        totalSize += 1
    }

    fun putAll(key: Key, values: Collection<Value>) {
        ensureSafeToAdd(values.size)
        map.getOrPut(key) {
            newSafeList.invoke()
        }.addAll(values)
        totalSize += values.size
    }

    fun putAll(multiMap: SafeMultiMap<Key, Value>) = {
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
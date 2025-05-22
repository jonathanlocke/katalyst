package jonathanlocke.katalyst.data.structures.maps

import jonathanlocke.katalyst.data.structures.SafeDataStructure
import jonathanlocke.katalyst.data.values.numeric.count.Count
import jonathanlocke.katalyst.status.StatusHandler

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
class SafeMap<Key : Any, Value> internal constructor(
    override val statusHandler: StatusHandler,
    override val metadata: SafetyMetadata,
    private val newUnsafeMap: (Count) -> MutableMap<Key, Value>,
) : SafeDataStructure(statusHandler, metadata), MutableMap<Key, Value> {

    private val map = newUnsafeMap(metadata.initialSize)

    fun copy(): SafeMap<Key, Value> {
        val copy = SafeMap(statusHandler, metadata, newUnsafeMap)
        copy.putAll(this)
        return copy
    }

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

    fun getOrPut(key: Key, defaultValue: () -> Value): Value {
        ensureSafeToAdd(1)
        return map.getOrPut(key, defaultValue)
    }

    fun computeIfAbsent(key: Key, mappingFunction: (Key) -> Value): Value {
        ensureSafeToAdd(1)
        return map.computeIfAbsent(key, mappingFunction)
    }

    fun merge(key: Key, value: Value, remappingFunction: (Value, Value) -> Value): Value {
        ensureSafeToAdd(1)
        throw UnsupportedOperationException()
    }

    override val size: Int get() = map.size
    override fun clear() = map.clear()
    override fun isEmpty(): Boolean = map.isEmpty()
    override fun containsKey(key: Key): Boolean = map.containsKey(key)
    override fun containsValue(value: Value): Boolean = map.containsValue(value)
    override fun get(key: Key): Value? = map.get(key)
    override val entries: MutableSet<MutableMap.MutableEntry<Key, Value>> get() = map.entries
    override val keys: MutableSet<Key> get() = map.keys
    override val values: MutableCollection<Value> get() = map.values
    override fun remove(key: Key): Value? = map.remove(key)
    override fun remove(key: Key, value: Value): Boolean = map.remove(key, value)
    override fun replace(key: Key, value: Value): Value? = map.replace(key, value)
    override fun replace(key: Key, oldValue: Value, newValue: Value): Boolean = map.replace(key, oldValue, newValue)
}

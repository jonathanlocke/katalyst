package jonathanlocke.katalyst.nucleus.language.collections.maps

/**
 * A map that allows multiple values to be associated with a single key.
 *
 * @param K The type of the key
 * @param V The type of the value
 *
 * @see MutableMap
 */
class MultiMap<K : Any, V : Any> {

    private val map: MutableMap<K, MutableList<V>> = mutableMapOf()

    fun put(key: K, value: V) {
        map.getOrPut(key) { mutableListOf() }.add(value)
    }

    fun putAll(key: K, values: Collection<V>) {
        map.getOrPut(key) { mutableListOf() }.addAll(values)
    }

    fun putAll(multiMap: MultiMap<K, V>) = multiMap.entries().forEach { (key, values) -> putAll(key, values) }

    operator fun get(key: K): List<V>? {
        return map[key]
    }

    fun remove(key: K, value: V): Boolean {
        val values = map[key] ?: return false
        val removed = values.remove(value)
        if (values.isEmpty()) {
            map.remove(key)
        }
        return removed
    }

    fun removeAll(key: K): List<V>? {
        return map.remove(key)
    }

    fun containsKey(key: K): Boolean = map.containsKey(key)

    fun containsEntry(key: K, value: V): Boolean = map[key]?.contains(value) ?: false

    fun keys(): Set<K> = map.keys

    fun values(): List<V> = map.values.flatten()

    fun entries(): List<Pair<K, List<V>>> = map.map { it.key to it.value.toList() }

    fun clear() = map.clear()

    fun isEmpty(): Boolean = map.isEmpty()

    fun size(): Int = map.size
}

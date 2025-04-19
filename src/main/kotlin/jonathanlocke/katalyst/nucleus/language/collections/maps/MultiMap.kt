package jonathanlocke.katalyst.nucleus.language.collections.maps

class MultiMap<K, V> {

    private val map: MutableMap<K, MutableList<V>> = mutableMapOf()

    fun put(key: K, value: V) {
        map.getOrPut(key) { mutableListOf() }.add(value)
    }

    fun putAll(key: K, values: Collection<V>) {
        map.getOrPut(key) { mutableListOf() }.addAll(values)
    }

    fun get(key: K): List<V>? {
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

    fun entries(): Map<K, List<V>> = map.mapValues { it.value.toList() }

    fun clear() = map.clear()

    fun isEmpty(): Boolean = map.isEmpty()

    fun size(): Int = map.size
}

package jonathanlocke.katalyst.reflection.properties.walker.sorters

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.walker.PropertyComparator

class SortByName : PropertyComparator {
    override fun compare(a: Property<*>?, b: Property<*>?): Int {
        requireNotNull(a)
        requireNotNull(b)
        return a.accessor.name.compareTo(b.accessor.name)
    }
}

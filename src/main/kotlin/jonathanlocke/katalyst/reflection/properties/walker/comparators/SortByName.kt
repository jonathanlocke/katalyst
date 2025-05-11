package jonathanlocke.katalyst.reflection.properties.walker.comparators

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.walker.PropertyComparator

class SortByName : PropertyComparator {
    override fun compare(a: Property?, b: Property?): Int {
        requireNotNull(a)
        requireNotNull(b)
        return a.property.name.compareTo(b.property.name)
    }
}

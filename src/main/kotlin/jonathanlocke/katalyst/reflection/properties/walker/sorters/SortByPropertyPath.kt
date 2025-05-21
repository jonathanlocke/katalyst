package jonathanlocke.katalyst.reflection.properties.walker.sorters

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.walker.PropertyComparator

class SortByPropertyPath : PropertyComparator {
    override fun compare(a: Property<*>, b: Property<*>): Int {
        return a.path.toPathString().compareTo(b.path.toPathString())
    }
}
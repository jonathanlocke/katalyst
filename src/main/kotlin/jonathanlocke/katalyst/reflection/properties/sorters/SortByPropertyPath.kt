package jonathanlocke.katalyst.reflection.properties.sorters

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.walker.PropertyComparator

class SortByPropertyPath : PropertyComparator {
    override fun compare(a: Property<*>, b: Property<*>): Int {
        return a.path.pathString().compareTo(b.path.pathString())
    }
}
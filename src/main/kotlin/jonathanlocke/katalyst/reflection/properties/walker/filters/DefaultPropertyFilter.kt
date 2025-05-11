package jonathanlocke.katalyst.reflection.properties.walker.filters

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.walker.PropertyFilter

class DefaultPropertyFilter : PropertyFilter {
    override fun test(property: Property): Boolean = true
}
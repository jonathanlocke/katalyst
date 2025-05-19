package jonathanlocke.katalyst.reflection.properties.walker.filters

import jonathanlocke.katalyst.reflection.properties.Property
import jonathanlocke.katalyst.reflection.properties.walker.PropertyExplorationFilter

class DefaultPropertyExplorationFilter : PropertyExplorationFilter {
    override fun test(property: Property<*>): Boolean {
        return property.name != "class" && property.name != "<unknown>"
    }
}
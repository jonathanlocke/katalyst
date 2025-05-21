package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property
import java.util.function.Predicate

fun interface PropertyExplorationFilter : Predicate<Property<*>> {
    fun and(that: PropertyExplorationFilter): PropertyExplorationFilter {
        return PropertyExplorationFilter { property -> this.test(property) && that.test(property) }
    }
}

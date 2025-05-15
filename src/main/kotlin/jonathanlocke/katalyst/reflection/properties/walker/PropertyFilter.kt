package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property
import java.util.function.Predicate

fun interface PropertyFilter : Predicate<Property<*>> {

    fun and(that: PropertyFilter): PropertyFilter =
        PropertyFilter { property -> test(property) && that.test(property) }
}

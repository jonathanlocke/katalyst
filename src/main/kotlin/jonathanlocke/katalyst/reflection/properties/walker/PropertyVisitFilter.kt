package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property
import java.util.function.Predicate

fun interface PropertyVisitFilter : Predicate<Property<*>>

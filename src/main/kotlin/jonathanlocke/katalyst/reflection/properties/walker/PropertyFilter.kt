package jonathanlocke.katalyst.reflection.properties.walker

import java.util.function.Predicate

fun interface PropertyFilter : Predicate<PropertyInformation>
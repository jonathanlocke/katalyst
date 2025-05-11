package jonathanlocke.katalyst.reflection.properties.walker

import jonathanlocke.katalyst.reflection.properties.Property

fun interface PropertyVisitor {
    fun atProperty(property: Property)
}

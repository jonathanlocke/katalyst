package jonathanlocke.katalyst.reflection.properties.walker

fun interface PropertyVisitor {
    fun atProperty(property: PropertyInformation)
}

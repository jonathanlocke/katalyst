package jonathanlocke.katalyst.reflection.properties

fun interface PropertyVisitor {
    fun atProperty(path: PropertyPath, property: Property<*>, value: Any?)
}

package jonathanlocke.katalyst.cripsr.reflection

fun interface PropertyVisitor {
    fun atProperty(path: PropertyPath, property: Property<*>, value: Any?)
}

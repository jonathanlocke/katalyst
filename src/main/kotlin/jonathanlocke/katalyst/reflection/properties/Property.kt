package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType

data class Property(
    val parent: Any?, val path: PropertyPath, val property: PropertyAccessor<*>, val value: Any?
) {
    val parentValueType = if (parent == null) null else ValueType.Companion.valueType(parent::class)
    val packageName = parentValueType?.qualifiedName ?: "<none>"

    override fun toString(): String = packageName + ":" + path.pathString() + " = " + value
}
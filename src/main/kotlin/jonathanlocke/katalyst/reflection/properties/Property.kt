package jonathanlocke.katalyst.reflection.properties

import jonathanlocke.katalyst.reflection.ValueType

data class Property<Value : Any>(
    val parent: Any?,
    val path: PropertyPath,
    val accessor: PropertyAccessor<Value>,
    val value: Value?,
) {
    val parentValueType = if (parent == null) null else ValueType.Companion.valueType(parent::class)
    val packageName = parentValueType?.qualifiedName ?: "<none>"
    val type = accessor.type()

    override fun toString(): String = packageName + ":" + path.pathString() + " = " + value
}